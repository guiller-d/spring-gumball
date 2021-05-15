package com.example.springgumball;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class GumballCommand {

    private String action ;
    private String message ;
    private String state ;
    private String timestamp ;
    private String hash ;

  public void setAction(String action) {
    this.action = action;
  }
  public void setMessage(String message) {
    this.message = message;
  }  
  public void setState(String state) {
     this.state = state;
  }  
  public void setTimestamp(String timestamp) {
     this.timestamp = timestamp;
  }  
  public void setHash(String hash) {
     this.hash = hash;
  }  

  public String getAction() {
    return this.action;
  }
  public String getMessage() {
    return this.message;
  }  
  public String getState() {
    return this.state;
  }  
  public String getTimestamp() {
    return this.timestamp;
  }  
  public String getHash() {
    return this.hash;
  }  
  


   
    
}