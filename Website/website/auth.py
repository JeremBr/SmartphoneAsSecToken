from random import random
from flask import Blueprint, render_template, request, flash, redirect, session
from .models import User
from . import db
import string
import random
import nacl.utils
from nacl.public import PrivateKey, Box
from nacl.signing import SigningKey
from flask import jsonify

auth = Blueprint('auth', __name__)

privkserver = PrivateKey.generate()
pubkserver = privkserver.public_key
print(pubkserver)

@auth.route('/', methods = ['GET', 'POST'])
def login():
    session.pop('credentials', None)
    session.pop('authenticated', None)
    if request.method == 'POST':
        email = request.form.get('email')
        password = request.form.get('password')

        user = User.query.filter_by(email=email).first()

        if user:
            if user.password == password:
                flash('Credentials accepted', category='success')
                #user.loginToken = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(10))
                db.session.commit()
                session['credentials'] = user.id
                return redirect('/authentication')
            else:
                flash('Incorrect Credentials', category='error')
        else:
            flash('Email does not exist', category='error')

    return render_template("login.html")


@auth.route('/keyexchange', methods = ['GET', 'POST'])
def keyexchange():
    try:
        user = User.query.filter_by(id=session['credentials']).first()
    except:
        flash('Login first', category='error')
        return redirect('/')
    if request.method == "POST":
        pubkuser = nacl.public.PublicKey(request.get('pubkuser'))
        server_box = Box(privkserver, pubkuser)
        print('Keys exchanged and serverbox generated')
    return 'Nope'


#TBD Make sure have been exchaged
@auth.route('/atestation', methods = ['GET', 'POST'])
def atestation():
    try:
        user = User.query.filter_by(id=session['credentials']).first()
    except:
        flash('Login first', category='error')
        return redirect('/')

    if request.method == 'POST':
        if request.get('createToken') == user.createToken:
            user.smartphoneLinked = 1
            print("Valid Token")
            db.session.commit()

            #Create signing key
            signing_key = SigningKey.generate()

            #Sign login token
            signed_token = signing_key.sign(bytes(user.loginToken, encoding='utf-8'))

            print(str(signed_token))

            #encrypt signed token to box
            encrypted = server_box.encrypt(signed_token)

            print('\n' + str(encrypted))

            # Obtain the verify key for a given signing key
            verify_key_server = signing_key.verify_key
            # Serialize the verify key to send it to a third party
            verify_key_server_bytes = verify_key_server.encode()

            return jsonify(encrypted=encrypted, verify_key_server_bytes=verify_key_server_bytes)

    return 'HOLA HOLA'


@auth.route('/authentication', methods = ['GET', 'POST'])
def authentication():
    try:
        user = User.query.filter_by(id=session['credentials']).first()
        if user.smartphoneLinked == 0:
            createToken = 'Activation token to be inserted in the smartphone app: ' + user.createToken
        else:
            createToken = 'Smartphone linked to the account'
    except:
        flash('Login first', category='error')
        return redirect('/')

    if request.method == 'POST':
        token = request.form.get('token')
        if token == user.loginToken:
            session['authenticated'] = True
            flash('You have successfully logged in to the Wallet Web Application', category='success')
            return redirect('/home_user')
        else:
            session.pop('credentials', None)
            flash('Incorrect token', category='error')
            return redirect('/')
    return render_template("authentication.html", createToken=createToken, username=user.username)

@auth.route('/deposit', methods = ['GET', 'POST'])
def deposit():
    if 'credentials' not in session or 'authenticated' not in session:
        flash('Login first', category='error')
        return redirect('/')
    elif request.method == 'POST':
        deposited = request.form.get('deposited')
        user = User.query.filter_by(id=session['credentials']).first()
        try:
            int(deposited) > 0
        except:
            flash('Enter an integer number', category='error')
            return redirect('/deposit')

        if int(deposited) > 0:
            user.money+=int(deposited)
            db.session.commit()
            flash('You have successfully deposited', category='success')
            return redirect('/home_user')
        else:
            flash('Incorrect amount', category='error')
            return redirect('/deposit')
    return render_template("deposit.html")

@auth.route('/send_money', methods = ['GET', 'POST'])
def send_money():
    if 'credentials' not in session or 'authenticated' not in session:
        flash('Login first', category='error')
        return redirect('/')
    elif request.method == 'POST':
        money_sent = request.form.get('money_sent')
        sent_to = request.form.get('sent_to')
        user = User.query.filter_by(id=session['credentials']).first()
        try:
            int(money_sent) > 0
        except:
            flash('Enter an integer number', category='error')
            return redirect('/send_money')
        receiver = User.query.filter_by(username=sent_to).first()
        if int(money_sent) > 0 and int(money_sent) < user.money:
            try:
                receiver.money += int(money_sent)
            except:
                flash('Username invalid', category='error')
                return redirect('/send_money')
            user.money -= int(money_sent)
            db.session.commit()
            flash('You have successfully money_sent', category='success')
            return redirect('/home_user')
        else:
            flash('Incorrect amount', category='error')
            return redirect('/send_money')
    return render_template("send_money.html")


@auth.route('/logout')
def logout():
    session.pop('credentials', None)
    session.pop('authenticated', None)
    flash('User successfully logged out', category='success')
    return redirect('/')

@auth.route('/register', methods = ['GET', 'POST'])
def sign_up():
    if request.method == 'POST':
        username = request.form.get('username')
        email = request.form.get('email')
        password = request.form.get('password')
        password1 = request.form.get('password1')

        if User.query.filter_by(email=email).first():
            flash('Email already exists', category='error')
        elif len(username) < 4:
            flash('Username needs to have at least 4 characters', category='error')
        elif len(email) < 3:
            flash('Email needs to have at least 3 characters', category='error')
        elif len(password) < 4:
            flash('Password needs to have at least 4 characters', category='error')
        elif len(password1) < 4:
            flash('Password needs to have at least 5 characters', category='error')
        elif password != password1:
            flash('Passwords do not match', category='error')
        else:
            createToken = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(10))
            new_user = User(username=username, email=email, password=password, money=0, createToken=createToken, smartphoneLinked=0)
            db.session.add(new_user)
            db.session.commit()
            flash('Account created', category='success')
            return redirect('/')

    return render_template("register.html")
