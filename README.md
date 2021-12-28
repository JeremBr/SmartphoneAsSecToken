# SmartphoneAsSecToken

IST Project - SIRS

Bernardo QUINTEIRO, Diogo LOPES, Jérémy BRETON  


## Plan


We login on bank acc with mail/password, on the website.
When we wants to access wallet we need password and token.
This token is on the app.

He has to be logged on the app to access the token.


## Cryptographie

Smartphone - Browser - Server

First RSA keys are shared in order to communicate.
Then they exchange AES and MACs keys, to later use AES-GCM which ensure Confidentiality and Integrity.


AES-GCM is an Authenticated Encryption algorithm. Which is based on Encrypt-then-MAC: c||S(Ki,c) with c=E(Ke,m)
So we need Ke for Encryption and Ki for MAC Signature


## References

[Links](All_Links.md)