<h4 align="center">Steps For Authenticating and Authorizing User Using JPA and MYSQL</h4>
<li>Override the AuthenticationManageBuilder and HttpSecurity Method from WebConfigurerAdapter</li>

<li>Specify authentication to be done by UserDetailsService and autowire it into the security configuration class

                @Autowired
                    private UserDetailsService userDetailsService;
                    
                @Override
                protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                    auth.userDetailsService(userDetailsService);
                }
                
                @Override
                protected void configure(HttpSecurity http) throws Exception {
                 http.authorizeRequests()
                // Most Restrictive To Least Restrictive in terms of order
                .antMatchers("static/css", "static/js").permitAll() //Static files permit for all users
                .antMatchers("/admin").hasRole("ADMIN") // .hasAnyRole("USER","ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/").permitAll()
                .and().formLogin();
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
