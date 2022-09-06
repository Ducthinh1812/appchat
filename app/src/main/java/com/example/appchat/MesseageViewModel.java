package com.example.appchat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appchat.Model.Chat;

import java.util.ArrayList;
import java.util.List;

public class MesseageViewModel extends ViewModel {
    private MutableLiveData<List<Chat>> chatMutableLiveData;
    private List<Chat> list;

    public MesseageViewModel() {
        chatMutableLiveData=new MutableLiveData<List<Chat>>();
        initdata();
    }

    private void initdata() {
        list=new ArrayList<>();
        chatMutableLiveData.setValue(list);
    }
    public MutableLiveData<List<Chat>> getChatMutableLiveData(){
        return chatMutableLiveData;
    }
    public void addmess(Chat chat){
        list.add(chat);
        chatMutableLiveData.setValue(list);
    }
}
