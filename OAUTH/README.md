<h2 align="center">OAUTH 2.0</h4>
<p>The auth in OAUTH stands for authorization and not authentication.</p>
<p>Used for authorization between services.</p>
<li> For ex. A photo printing service where the user upload their photos and the service prints the photo for them but now a days mostly users store photo on cloud therefore it would be a good option to for the user to directly upload the photo from their google drive</li>
<li> To support this OAUTH comes into existence if both the application have OAUTH implemented the photo printing service will go google and ask for the files then google will instead forward the request to the user and ask the user to verify whether the service is legit or not(i.e asks for permissions).  </li>
<li>Once the user verifies that the service is legit the google in turns shares the requested data and gives a JWT token (LIMITED ACCESS) for further communication </li>
<li>Example of a OAuth Screen presented to the user</li>

![OAUTHExample](img/OAUTH1.png)

NOTE - In both the service i.e the Photo Printing Service and Google drive the user is already authenticated and for direct communication between these 2 services OAUTH is used, Which makes it pretty clear that in this case OAUTH is user only for Authorization and is most of the cases OAUTH is used for AUTHORIZATION.