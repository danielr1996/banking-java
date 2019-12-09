INSERT INTO benutzer (name, passwordhash) VALUES ('user1','password1');
INSERT INTO benutzer (name, passwordhash) VALUES ('user2','password2');

INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('42601f3b-6e91-4c80-bb11-c5a21d98fc57', '76050101','Reich', '518222', 'user1','P20');
INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('6fdf50c3-938f-4aa2-a2b7-a2849b8fc25a', '76050102','Reich', '518221', 'user1','P20');
INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('d57ba00b-fdc3-4a18-b16f-fb967e02072a', '76050103','Reich', '518223', 'user2','P20');
INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('4b2d5fca-cb8a-44a9-8fe2-6842e519956d', '76050104','Reich', '518224', 'user2','P20');

INSERT INTO saldo(betrag, datum, konto_id) VALUES (500.0, current_timestamp , '42601f3b-6e91-4c80-bb11-c5a21d98fc57');
INSERT INTO saldo(betrag, datum, konto_id) VALUES (400.0, current_timestamp, '6fdf50c3-938f-4aa2-a2b7-a2849b8fc25a');
INSERT INTO saldo(betrag, datum, konto_id) VALUES (300.0, current_timestamp, 'd57ba00b-fdc3-4a18-b16f-fb967e02072a');
INSERT INTO saldo(betrag, datum, konto_id) VALUES (100.0, current_timestamp, '4b2d5fca-cb8a-44a9-8fe2-6842e519956d');

-- INSERT INTO transaktionspartner(iban, bic, blz, name) VALUES('DE12500?0?170648489890', 'SSKNDE12345', '76050101', 'User 1');

INSERT INTO buchung(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, konto_id, other_partner_iban, self_partner_iban) VALUES(
'201910280705', 10.0, '2019-10-28', 'Buchungstext', '2019-10-28', 'Verwendungszweck 1', 'EUR', '42601f3b-6e91-4c80-bb11-c5a21d98fc57', 'DE45XXXX', 'DE78XXXX');
INSERT INTO buchung(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, konto_id, other_partner_iban, self_partner_iban) VALUES(
'201910270705', 20.0, '2019-10-27', 'Buchungstext', '2019-10-27', 'Verwendungszweck 2', 'EUR', '42601f3b-6e91-4c80-bb11-c5a21d98fc57', 'DE45XXXX', 'DE78XXXX');
INSERT INTO buchung(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, konto_id, other_partner_iban, self_partner_iban) VALUES(
'201910260705', 30.0, '2019-10-26', 'Buchungstext', '2019-10-26', 'Verwendungszweck 3', 'EUR', '42601f3b-6e91-4c80-bb11-c5a21d98fc57', 'DE45XXXX', 'DE78XXXX');


INSERT INTO buchung(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, konto_id, other_partner_iban, self_partner_iban) VALUES(
'201910250705', 10.0, '2019-10-25', 'Buchungstext', '2019-10-25', 'Verwendungszweck 4', 'EUR', '6fdf50c3-938f-4aa2-a2b7-a2849b8fc25a', 'DE45XXXX', 'DE78XXXX');
INSERT INTO buchung(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, konto_id, other_partner_iban, self_partner_iban) VALUES(
'201910240705', 10.0, '2019-10-24', 'Buchungstext', '2019-10-24', 'Verwendungszweck 5', 'EUR', 'd57ba00b-fdc3-4a18-b16f-fb967e02072a', 'DE45XXXX', 'DE78XXXX');
INSERT INTO buchung(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, konto_id, other_partner_iban, self_partner_iban) VALUES(
'201910230705', 10.0, '2019-10-23', 'Buchungstext', '2019-10-23', 'Verwendungszweck 6', 'EUR', '4b2d5fca-cb8a-44a9-8fe2-6842e519956d', 'DE45XXXX', 'DE78XXXX');

INSERT INTO transaktionspartner(iban, bic, blz, name) VALUES ('DE45XXXX','SSKNDXX77','76050101','Daniel Richter');
INSERT INTO transaktionspartner(iban, bic, blz, name) VALUES ('DE78XXXX','SSKNDXX77','76050101','Sparkasse Nürnberg');
