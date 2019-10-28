INSERT INTO USER(name, password) VALUES ('user1','password1');
INSERT INTO USER(name, password) VALUES ('user2','password2');

INSERT INTO KONTO(id, blz, password, kontonummer, userid) VALUES ('42601f3b-6e91-4c80-bb11-c5a21d98fc57', '76050101','Reich', '518221', 'user1');
INSERT INTO KONTO(id, blz, password, kontonummer, userid) VALUES ('6fdf50c3-938f-4aa2-a2b7-a2849b8fc25a', '76050102','Reich', '518222', 'user1');
INSERT INTO KONTO(id, blz, password, kontonummer, userid) VALUES ('d57ba00b-fdc3-4a18-b16f-fb967e02072a', '76050103','Reich', '518223', 'user2');
INSERT INTO KONTO(id, blz, password, kontonummer, userid) VALUES ('4b2d5fca-cb8a-44a9-8fe2-6842e519956d', '76050104','Reich', '518224', 'user2');

INSERT INTO SALDO(id, betrag, datum, kontoid) VALUES ('ffceff9a-4dc9-4912-af0b-ca9ab1c0ff0c', 500.0, CURRENT_TIMESTAMP(), '42601f3b-6e91-4c80-bb11-c5a21d98fc57');
INSERT INTO SALDO(id, betrag, datum, kontoid) VALUES ('bc19f203-ae5b-465e-bfef-22fb87f1298a', 400.0, CURRENT_TIMESTAMP(), '6fdf50c3-938f-4aa2-a2b7-a2849b8fc25a');
INSERT INTO SALDO(id, betrag, datum, kontoid) VALUES ('69fcd811-a3e3-4dcc-877b-a5f263216177', 300.0, CURRENT_TIMESTAMP(), 'd57ba00b-fdc3-4a18-b16f-fb967e02072a');
INSERT INTO SALDO(id, betrag, datum, kontoid) VALUES ('c4f19460-fd23-4f10-a9cc-5973998a79e9', 100.0, CURRENT_TIMESTAMP(), '4b2d5fca-cb8a-44a9-8fe2-6842e519956d');

-- INSERT INTO TRANSAKTIONSPARTNER(iban, bic, blz, name) VALUES('DE12500?0?170648489890', 'SSKNDE12345', '76050101', 'User 1');

INSERT INTO BUCHUNG(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, kontoid, otherpartner_iban, selfpartner_iban) VALUES(
'201910280705', 10.0, '2019-10-28', 'Buchungstext', '2019-10-28', 'Verwendungszweck 1', 'EUR', '42601f3b-6e91-4c80-bb11-c5a21d98fc57', null, null);
INSERT INTO BUCHUNG(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, kontoid, otherpartner_iban, selfpartner_iban) VALUES(
'201910270705', 20.0, '2019-10-27', 'Buchungstext', '2019-10-27', 'Verwendungszweck 2', 'EUR', '42601f3b-6e91-4c80-bb11-c5a21d98fc57', null, null);
INSERT INTO BUCHUNG(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, kontoid, otherpartner_iban, selfpartner_iban) VALUES(
'201910260705', 30.0, '2019-10-26', 'Buchungstext', '2019-10-26', 'Verwendungszweck 3', 'EUR', '42601f3b-6e91-4c80-bb11-c5a21d98fc57', null, null);


INSERT INTO BUCHUNG(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, kontoid, otherpartner_iban, selfpartner_iban) VALUES(
'201910280705', 10.0, '2019-10-28', 'Buchungstext', '2019-10-28', 'Verwendungszweck 4', 'EUR', '6fdf50c3-938f-4aa2-a2b7-a2849b8fc25a', null, null);
INSERT INTO BUCHUNG(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, kontoid, otherpartner_iban, selfpartner_iban) VALUES(
'201910280705', 10.0, '2019-10-28', 'Buchungstext', '2019-10-28', 'Verwendungszweck 5', 'EUR', 'd57ba00b-fdc3-4a18-b16f-fb967e02072a', null, null);
INSERT INTO BUCHUNG(id, betrag, buchungstag, buchungstext, valutadatum, verwendungszweck, waehrung, kontoid, otherpartner_iban, selfpartner_iban) VALUES(
'201910280705', 10.0, '2019-10-28', 'Buchungstext', '2019-10-28', 'Verwendungszweck 6', 'EUR', '4b2d5fca-cb8a-44a9-8fe2-6842e519956d', null, null);
