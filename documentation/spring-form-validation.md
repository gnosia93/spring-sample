
```
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

	final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
		
		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);
		
		return "registration";
	}
	
	
	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("user") @Valid UserDto userDto) {
		
		return "registration";
	}
}
```


```


```
