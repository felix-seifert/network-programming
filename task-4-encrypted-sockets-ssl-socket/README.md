# Simple Game via HTTPS of Clients With Server

This implementation is based on the one from [task 2](https://github.com/Sanskar95/HTTPServerClientGame) and consist 
out of two classes which can be started: `GameServer` and `ConnectionSimulation`. The former class starts a server 
which a client can contact to play the game. The latter class simulates a given amount of clients who sequentially play 
a whole round of the game with the server automatically. The implementation of the game of task 4 extends the simple 
game between client and server with a secure encryption via SSL sockets.

## GameServer

To start the class `GamesServer`, you have to provide the port number it should listen to as an argument: 
`java GameServer <port-number>`. The port number could for example be 5000. The server then starts and listens to the 
provided port. The server reacts on GET and POST requests and the connection is encrypted. The server has to be called 
with `https://<host-address>:<port-number>/http_post.html` to start the game. Each time, a new thread of the class 
`ClientManager` is started.
                                                                                          
The client receives a question to guess a number between 1 and 100 which the server chose (for simplicity, the server 
uses a fixed number). After the clients sends the guessed value via a field, it receives a statement whether the guess 
is too high, too low or correct. It can continue its guesses because a client id gets assigned as a cookie to each 
client. This helps the server to identify the past guesses of this client.

## SSL on Server Side

For the server side, a keystore with the public and the private key have to be loaded that the server knows how to 
decrypt the messages which any client encrypted with the public key. The keystore exists in the [resources folder of 
the application](/src/main/resources). After loading the keystore as a `KeyManager` with its password (password is 
`localhost`), an `SSLContext` is needed to eventually create an encrypted server socket (`SSLServerSocket`). An 
`SSLContext` can be initialised with both a `KeyManager` and a `TrustManager`. However, the server side does not need a 
`TrustManager` as the classical SSL encryption and the more modern TLS encryption have only a unilateral trust, i.e. 
only the client trusts the server and the server does not have to trust the clients.

As the used certificate is self-signed and browsers do not know about this, the certificate has to be imported. For 
convincing Chrome to accept this certificate (and therefore the website) as secure, an own certification authority (CA) 
has to be created which then have to be trusted by Chrome. Afterwards, Chrome would accept certificates of this CA. 
Another option is to create an exception for this website. A browser like Firefox can create an exception and can 
connect to the relevant website to get the certificate from there.

## ConnectionSimulation

The class `ConnectionSimulation` needs with the two arguments port number and the number of simulated rounds: 
`java ConnectionSimulation <port-number> <number-simulated-rounds>`. The port number specifies to which port the 
simulated clients should connect to. The second argument specifies how many rounds should be played.

The programme connects to the given port and guesses a number. Based on the result, the simulation then tries to 
find a better number until it succeeds. At the end of each round, the number of needed turns to win this round is 
printed on the console. At the end of the whole simulation, the number of the played rounds and the average number of 
turns per round is printed on the console.

## Client Side Connection to SSL Server

To contact an encrypted page, the client needs the certificate which includes the public key of that page. The 
certificate also exists within the [resources folder of the application](/src/main/resources). As the certificate is a 
self-signed certificate which does not exist in Java's trust manager, the goal is to create an own `TrustManager` which 
includes the needed certificate. The first step is therefore to create a `KeyStore` whith the certificate in it which 
the new `TrustManager` then refers to. With this `TrustManager`, an `SSLContext` is created which can be used to 
instantiate clients.

Usually, an `HttpClient` can be used to execute multiple requests. However, this somehow does not work and a new 
`HttpCleint` is needed for every request. Therefore, every time `ConnectionSimulations` fires an `HttpRequest` to the 
server a new `HttpClient` is build. This action consumes many resources and makes the simulated connections 
significantly slower than the unencrypted ones. 