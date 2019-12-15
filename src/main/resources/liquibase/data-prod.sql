INSERT INTO benutzer (name, passwordhash, salt, iterations)
VALUES ('danielr1996', 'ce161ff4263bef54d4386e093e7d0986c6506664b16af971e93f2995beb646cb',
        '5d98e8b0ea7b006d65b0fa876eeae9c0f4df2c08', 200000);
INSERT INTO konto(id, blz, passwordhash, bankaccount, user_id, tanmedia, kontonummer)
VALUES ('42601f3b-6e91-4c80-bb11-c5a21d98fc57', '76050101', 'Reich', '518222', 'danielr1996', 'P20','5182225');
