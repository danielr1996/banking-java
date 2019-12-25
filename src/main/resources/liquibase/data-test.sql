-- password1
INSERT INTO benutzer (name, passwordhash, salt, iterations) VALUES ('user1','88312dd9ebe5c0e6af351330191c8ff1f1b2391085ad59b610655113ca1dc6da','2ed04a118578ba2c14d89f7283805afe75ea9f71',200000);
-- password2
INSERT INTO benutzer (name, passwordhash, salt, iterations) VALUES ('user2','9aa93ede079da28da894fbd235737dd7b98b8e84e6f9e8c7ca41b7522eafa036','b677bd37c79c689767b77cd9b5168089ea7c9d3f',200000);

INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('42601f3b-6e91-4c80-bb11-c5a21d98fc57', 'BLZ','RexohD34Ey6P5TroHv1ocA==', 'Kontonummer', 'user1','TanMedia');
INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('6fdf50c3-938f-4aa2-a2b7-a2849b8fc25a', 'BLZ','RexohD34Ey6P5TroHv1ocA==', 'Kontonummer', 'user1','TanMedia');
INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('d57ba00b-fdc3-4a18-b16f-fb967e02072a', 'BLZ','RexohD34Ey6P5TroHv1ocA==', 'Kontonummer', 'user2','TanMedia');
INSERT INTO konto(id, blz, passwordhash, kontonummer, user_id, tanmedia) VALUES ('4b2d5fca-cb8a-44a9-8fe2-6842e519956d', 'BLZ','RexohD34Ey6P5TroHv1ocA==', 'Kontonummer', 'user2','TanMedia');

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

INSERT INTO transaktionspartner(iban, bic, blz, name) VALUES ('DE45XXXX','SSKNDXX77','76050101','Hans Peter');
INSERT INTO transaktionspartner(iban, bic, blz, name) VALUES ('DE78XXXX','SSKNDXX77','76050101','Geld Insitut');
