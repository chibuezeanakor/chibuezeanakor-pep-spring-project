package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Message addMessage(Message message){
        return messageRepository.save(message);
    }

    @Transactional
    public Message updateMessage(Integer message_id, Message message){
        Message existingMessage = getMessageById(message_id);

        if (!existingMessage.equals(null)) {
            existingMessage.setMessageText(message.getMessageText());
            return messageRepository.save(existingMessage);
        }

        return null;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer message_id) {
        Optional<Message> messageOptional = messageRepository.findById(message_id);

        if (messageOptional.isPresent()) {
            return messageOptional.get();
        }

        return null;
    }

    public List<Message> getAllMessagesByAccountId(Integer account_id) {
        return messageRepository.findMessagesByPostedBy(account_id);
    }

    public Message deleteMessage(Integer message_id){
        Message existingMessage = getMessageById(message_id);

        if (!existingMessage.equals(null)) {
            messageRepository.delete(existingMessage);;
            return existingMessage;
        }

        return null;
    }
}
