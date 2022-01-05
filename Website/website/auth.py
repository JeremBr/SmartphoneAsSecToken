from random import random
from flask import Blueprint, render_template, request, flash, redirect, session
from .models import User
from . import db

auth = Blueprint('auth', __name__)

@auth.route('/', methods = ['GET', 'POST'])
def login():
    if request.method == 'POST':
        email = request.form.get('email')
        password = request.form.get('password')

        user = User.query.filter_by(email=email).first()

        if user:
            if user.password == password:
                flash('Credentials accepted', category='success')
                user.token = int(random()*100)
                db.session.commit()
                session['credentials'] = user.id
                return redirect('/authentication')
            else:
                flash('Incorrect Credentials', category='error')
        else:
            flash('Email does not exist', category='error')

    return render_template("login.html")

@auth.route('/authentication', methods = ['GET', 'POST'])
def authentication():
    if 'credentials' not in session:
        flash('Login first', category='error')
        return redirect('/')
    elif request.method == 'POST':
        token = request.form.get('token')
        user = User.query.filter_by(id=session['credentials']).first()
        if token == user.token:
            session['authenticated'] = True
            flash('You have successfully logged in to the Wallet Web Application', category='success')
            return redirect('/home_user')
        else:
            session.pop('credentials', None)
            flash('Incorrect token', category='error')
            return redirect('/')
    return render_template("authentication.html")

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
            new_user = User(username=username, email=email, password=password, money=0)
            db.session.add(new_user)
            db.session.commit()
            flash('Account created', category='success')
            return redirect('/')

    return render_template("register.html")
