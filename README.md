<h2 align ="center">Spring Security</h2>

<h2 align ="center"> 5 Core Concepts of Spring Security</h2>

<h3 align="center"><strong><li>AUTHENTICATION</strong> - Who Are You ?</l1></h3>
<p><strong>Knowledge Based Authentication</strong> - Authentication depending on username and password which is a type of knowledge you have </p>
<p><strong>Possesive Authentication</strong> - OTP, Text Messages on your phone </p>
<p><strong>Multi Factor or 2 Factor Authentication</strong> - Knowledge Based + Possesive Authentication </p>
<br/>
<h3 align="center"><strong><li>AUTHORIZATION</strong> - I know who you are but are you allowed to do that ?</h3></li>
<br/>
<h3 align="center"><strong><li>PRINCIPAL</strong> - The Currently Logged in user or the user with the particular account</li></h3>
<br/>
<h3 align="center"><strong><li>AUTHORITIES/PERMISSIONS/GRANTED AUTHORITY</strong></l1> </h3>
<p>Permissions allowed for a specific users and then the user is authorized to do that action</p>
<p>Ex In a retail store A Clerk can do_checkout, make_store_announcements but a Manager can do_checkout, make_store_announcements and view_financial_record </p>
<p>Fine Granual Control</p>
<br/>
<li><h3 align="center"><strong>ROLES</strong></h3></li>
<p>Roles are a group of authorities/permissions that are assigned toghether i.e ROLE_MANAGER will have do_checkout, make_store_announcements and view_financial_record and ROLE_CLERK will have do_checkout, make_store_announcements</p>
<p>It makes easier to group permission and assign them to the user to be consistent </p>
<p>Coarse Grained</p>

<hr/>
<h2 align="center"><strong>How Spring Authentication Works?</strong></h2>
<p>Spring Security is basically an filter which block each request coming into the application and then process on it</p>
<p>Spring Security is applied on the entire application and not on a particular part/URL</p>

![Authetication](Img/AuthenticationSpring.png)

<p>In Spring Security when the authentication is successfull the authentication return Information about the logged in user</p>
<p>It Keeps Track of the both input(User credentials) and output(verified or not) using the object of Type AUTHENTICATION it is an internal spring security interface  </p>
<p>The Authentication objects the credentials before authentication and then the PRINCIPAL(Verified User Information) after authentication</p>
<hr/>

![Authentication1](Img/AuthenticationSpring1.png)

<p>Authentication Provider Is Responsible for doing the actual Authentication it is an interface having the method authenticate.</p>
<p>We need to have implementation of this interface in our application and inform Spring Security</p>
<p>User Enters his username and password, Spring Security puts this into the AUTHENTICATION object it goes to the implementation of the Authentication Provider and calls the authenticate method. If the credentials are right it then return the information about the currently logged in user in the Same AUTHENTICATION object</p>
<hr/>

![Authentication2](Img/AuthenticationSpring2.png)

<p>Each Application have more than 1 way to authenticate users. For ex UserName and Password based, OAuth based or LDAP based. </p>
<p>Therefore there can multiple Authentication Provider in an application.</p>
<p>To Coordinate Between all of these Authentication Provider there is a PROVIDERMANAGER</p>

<hr/>

![Authentication3](Img/AuthenticationSpring3.png)

<p>The ProviderManager asks all the AuthenticationProvider for the authentication type they support. To Achieve this Each authentication providers has a additional method Called Support().</p>
 <p>This is the method which is called by Provider Manager. Therefore each authentication provider has a Supports method.</p>

<hr/>

![Authentication4](Img/AuthenticationSpring4.png)
<p>For the Authentication provider to do the job the Provider needs to have access to the db where the userdetails are stored and retrieve it and verify it </p>
<p>To Retrieve the UserInformation is Abstracted By Spring Security Into A class called by UserDetailsService which has a method loadUserByUserName which gives the User as an object</p>

<hr/>
<h3 align="center">The Final Authentication Picture</h3>

![AuthenticaionFinal](Img/AuthenticaionFinal.png)


<hr/>
<h4 align="center">Steps For Authenticating and Authorizing User Using JPA and MYSQL</h4>
<li>Override the AuthenticationManageBuilder and HttpSecurity Method from WebConfigurerAdapter</li>

<li>Specify authentication to be done by UserDetailsService and autowire it into the security configuration class

                @Autowired
                    private UserDetailsService userDetailsService;
                    
                @Override
                protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                    auth.userDetailsService(userDetailsService);
                }
</li>


<li>
Create an User Entity mapping to the db table with getters and setter which is not shown below.

    @Entity
    @Table(name = "user")
    public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "roles")
    private String roles;
    
    }
    
</li>


<li>
Create an User Repository Interface extending JPA Repository and adding an additional method of findUserByUsername

    public interface UserRepository extends JpaRepository<User, Integer> {
        Optional<User> findByUserName(String UserName);
    }
    
</li>

<li>
Create a Custom UserDetailsService as defined in the security config implementing the UserDetailsService Interface.</li>
<li>We also need to Override an method loadUserByUsername which return an UserDetails instance and pass the user returned from our UserRepository to the CustomUserDetailsClass</li>


    @Service
    public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        Optional<User> user = userRepository.findByUserName(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found" + username));

        return user.map(usr->new MyUserDetails(usr)).get();
        }
    }

<li>The Custom UserDetails need to implement the UserDetails Class and override all the method in the class.</li>
 <li>This Class plays an major role as it returns the user with all the validation including the authorities need by Spring security to authenticate the user. </li>
<li>In the UserDetails Class the Authority list is the collection of SimpleGrantedAuthorities therefore we need to convert   the roles we Recieve from the user repository to the SimpleGrantedAuthorities.</li>
    
    public class MyUserDetails implements UserDetails {
    
        private String userName;
        private String password;
        private boolean active;
        private List<GrantedAuthority> authorityList;
    
        public MyUserDetails(User user) {
            // taking the user object from the db and converting it into the UserDetails Object
            this.userName = user.getUserName();
            this.password = user.getPassword();
            this.active = user.isActive();
            this.authorityList = Arrays.stream(user.getRoles().split(",")).map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        }
    
        public MyUserDetails() {
    
        }
    
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityList;
        }
    
        @Override
        public String getPassword() {
            return password;
        }
    
        @Override
        public String getUsername() {
            return userName;
        }
    
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
    
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
    
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
    
        @Override
        public boolean isEnabled() {
            return active;
        }
    }