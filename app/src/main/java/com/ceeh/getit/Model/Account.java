package com.ceeh.getit.Model;

public class Account {
    private User _user;
    private int _productive;

    public Account(User user) {
        this._user = user;
        _productive = 0;
    }

    public User get_user() {
        return _user;
    }

    public int get_productive() {
        return _productive;
    }

    public void set_productive(int _productive) {
        this._productive = _productive;
    }

    @Override
    public String toString() {
        return _user.toString();
    }
}
