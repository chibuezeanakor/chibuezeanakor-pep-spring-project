package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;

import com.example.service.AccountService;
import com.example.service.MessageService;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {
    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MessageRepository messageRepository;

    public SocialMediaController() {
        this.accountService = new AccountService(accountRepository);
        this.messageService = new MessageService(messageRepository);
    }

    @PostMapping("/register")
    private @ResponseBody ResponseEntity<Account> register(@RequestBody Account account) {
        if (accountService.isExistingUsername(account.getUsername())) {
            return ResponseEntity.status(409).body(null);
        }
        else if (!account.getUsername().isEmpty() && 
                account.getPassword().length() >= 4 && 
                !accountService.isExistingUsername(account.getUsername())) {
            Account newAccount = accountService.addAccount(account);
            return ResponseEntity.status(200).body(newAccount);
        }
        else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/login")
    private @ResponseBody ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.accountLogin(account.getUsername(), account.getPassword());
        if(loggedInAccount == null){
            return ResponseEntity.status(401).body(null);
        }else{
            return ResponseEntity.status(200).body(loggedInAccount);
        }
    }

    @PostMapping("/messages")
    private @ResponseBody ResponseEntity<Message> postMessage(@RequestBody Message message) {
        if (!message.getMessageText().isEmpty() && message.getMessageText().length() <= 255 && accountService.doesAccountExist(message.getPostedBy())) {
            Message newMessage = messageService.addMessage(message);
            return ResponseEntity.status(200).body(newMessage);
        }
        else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/messages")
    private @ResponseBody ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    @PatchMapping("/messages/{message_id}")
    private @ResponseBody ResponseEntity<Integer> updateMessage(@RequestBody Message message, @PathVariable Integer message_id) {
        if (!message.getMessageText().isEmpty() &&
            message.getMessageText().length() <= 255 &&
            messageService.getMessageById(message_id) != null &&
            messageService.updateMessage(message_id, message) != null) {
            return ResponseEntity.status(200).body(1);
        }
        else {
            return ResponseEntity.status(400).body(null);
        }
        
    }

    @GetMapping("/messages/{message_id}")
    private @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/accounts/{account_id}/messages")
    private @ResponseBody ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable Integer account_id) {
        return ResponseEntity.status(200).body(messageService.getAllMessagesByAccountId(account_id));
    }

    @DeleteMapping("/messages/{message_id}")
    private @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable Integer message_id) {
        if(messageService.deleteMessage(message_id) == null){
            return ResponseEntity.status(200).body(null);
        }else{
            return ResponseEntity.status(200).body(1);
        }
    }
}
