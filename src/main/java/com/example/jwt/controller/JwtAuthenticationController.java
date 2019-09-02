package com.example.jwt.controller;


import com.example.jwt.dto.UserDTO;
import com.example.jwt.request.JwtRequest;
import com.example.jwt.response.JwtResponse;
import com.example.jwt.security.JwtTokenUtil;
import com.example.jwt.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

//        boolean status = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());


//        if (status = false) {
//
//            System.out.println("Unauthorised user");
//            new Exception("Unauthorised user");
//        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.getPassword());

        System.out.println(token);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {

        System.out.println(user);

        return ResponseEntity.ok(userDetailsService.save(user));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

//            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

//            boolean status = auth.isAuthenticated();
//
//            if (status == false) {
//                return false;
//            }
//            return true;

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}