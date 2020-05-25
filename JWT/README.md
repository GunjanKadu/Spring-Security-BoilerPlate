
<h3 align="center"><strong>HTTP/JWT/SESSION TOKEN</h3>
<p>HTTP is a STATELESS PROTOCOL i.e for every interaction in HTTP everything needs to be there in the interaction no state from before is stored to authenticate.</p>
<p>Therefore for every request the user need to authenticate themselves and is done by using token, there are 2 types of implementation of token based authorization</p>
<li>SESSION TOKEN </li> 
<p>As soon as the user authenticates the server creates a session and returns back the id to the user. So for every request the client passes the id to the server for every request. The server then looks it up and authenticates the client. </p>
<p>This Pattern was working fine but it assumes something, it assumes that there is always One Monolithic server but it is past as Shown below. Which is no longer the case these days</p>

![Monolithic](Img/Monolithic.png)


<p>But now days modern web apps looks like the following below. Each web app has multiple servers sharing the load by LOAD balancer and then the load balancer decides where to route the new request to.</p>

<p>But Here the problem is if the request for authenticating the user is sent to server 1 then server 1 creates a session for that user which the other server 2 and 3 have no idea about. Therefore for the next request if the load balancer routes the request to server 2  the user would'nt be authenticated</p>

![NotMono](Img/NotMono.png)


<p>The above problem can be solved by using redis cache which is acts as a shared memory for all the servers and the server validates each request from the this cache. The drawback is if the redis goes down then all the server instances go down. </p>

<p>This was solved by using the sticky sessions where the load balancer remember which server stored the session for that specific client and redirects all the other request from that client to that particular server which again created a problem of scalability and not adaptive to microservices as now web apps users multiple microservices and it would'nt be possible to track all the clients in this case.</p>

<hr/>
<p>Therefore a new approach was needed to solve it and JSON web token came into place</p>

<li>JSON WEB TOKEN  </li>
<p>When a client authenticates the server instead of saving the user information in the state of the server and returning the id as the token, it return a SIGNED  user information as the token in the form of JSON Payload.
<p>With Each request the client sends this SIGNED JSON token to the server indicating it's credentials, therefore the server isn't saving anything every time, with each request the server verifies the client on the base of the token.</p>


<li>Session Id tokens are reference tokens i.e the refer to a value stored in the state of the server.</li>
<li>JWT are value based token they contain value itself.</li>
