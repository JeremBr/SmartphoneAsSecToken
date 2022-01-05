from website import create_app

app = create_app()

if __name__ == '__main__':
    app.run(debug=True) # basta correr uma vez o website uma vez, ele faz as alteraçoes sozinho
