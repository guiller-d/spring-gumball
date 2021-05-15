package com.example.springgumball;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

import com.example.gumballmachine.GumballMachine;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Base64.Encoder;

@Slf4j
@Controller
@RequestMapping("/")
public class GumballMachineController {

    private static String key = "kwRg54x2Go9iEdl49jFENRM12Mp711QI" ;
    private java.util.Base64.Encoder encoder = java.util.Base64.getEncoder() ;

    private static byte[] hmac_sha256(String secretKey, String data) {
        try {
          Mac mac = Mac.getInstance("HmacSHA256") ;
          SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256") ;
          mac.init(secretKeySpec) ;
          byte[] digest = mac.doFinal(data.getBytes()) ;
          return digest ;
        } catch (InvalidKeyException e1) {
          throw new RuntimeException("Invalid key exception while converting to HMAC SHA256") ;
        } catch (NoSuchAlgorithmException e2) {
          throw new RuntimeException("Java Exception Initializing HMAC Crypto Algorithm") ;
        }
      }


    @GetMapping
    public String getAction( @ModelAttribute("command") GumballCommand command, Model model, HttpSession session) {
      
        GumballModel g = new GumballModel() ;
        g.setModelNumber( "SB102927") ;
        g.setSerialNumber( "2134998871109") ;
        model.addAttribute( "gumball", g ) ;
        
        GumballMachine gm = new GumballMachine() ;
        String message = gm.toString() ;
        session.setAttribute( "gumball", gm) ;

        String session_id = session.getId() ;


        byte[] hash = hmac_sha256( key, session_id ) ;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        //set state
        command.setState( gm.getState().getClass().getName() ) ;
        //set timestamp
        command.setTimestamp(timeStamp.toString());
        //set hash
        command.setHash(encoder.encodeToString(hash));

        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
  
        } catch (Exception e) { }
        model.addAttribute( "timeStamp", timeStamp.toString() ) ;
        model.addAttribute("hash", encoder.encodeToString(hash));
        model.addAttribute( "session", session_id ) ;
        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;

        return "gumball" ;

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") GumballCommand command,  
                            @RequestParam(value="action", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) {
    

        HttpSession session = request.getSession() ;
        GumballMachine gm = (GumballMachine) session.getAttribute("gumball") ;


        if ( action.equals("Insert Quarter") ) {
            gm.insertQuarter() ;

        }
        if ( action.equals("Turn Crank") ) {
            command.setMessage("") ;
            gm.turnCrank() ;
        }
        session.setAttribute( "gumball", gm) ;

        //message
        String message = gm.toString() ;
        //esession id
        String session_id = session.getId() ;      
        //use the session id for hashing
        byte[] hash = hmac_sha256( key, session_id ) ;  
        //timestamp
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        //set state
        command.setState( gm.getState().getClass().getName() ) ;
        //set timestamp
        command.setTimestamp(timeStamp.toString());
        //set hash
        command.setHash(encoder.encodeToString(hash));

        String state = action;

        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
  
        } catch (Exception e) { }
        model.addAttribute( "state", state ) ;
        model.addAttribute( "timeStamp", timeStamp.toString() ) ;
        model.addAttribute("hash", encoder.encodeToString(hash));
        model.addAttribute( "session", session_id ) ;
        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;
     

        if (errors.hasErrors()) {
            return "gumball";
        }
        log.info( "Action: " + action ) ;
        log.info( "Command: " + command ) ;
    

        return "gumball";
    }

}