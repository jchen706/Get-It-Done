package com.ceeh.getit.Model;

import java.util.Date;

public class Task {


    private long task_id;
    private String _status;
    private String _name;
    private String _dueDate;
    private String _time;
    private String _description;
    private int _repeatingMinutes;


    public Task(long id ,String name, String _status, String description, String dueDate, String _time, int _repeatingMinutes) {
        this.task_id = id;
        this._name = name;
        this._status = _status;
        this._dueDate = dueDate;
        this._time = _time;
        this._description = description;
        this._repeatingMinutes = _repeatingMinutes;

    }



    public Task(String name, String _status, String description, String dueDate, String _time, int _repeatingMinutes) {
       this._name = name;
       this._status = _status;
       this._dueDate = dueDate;
       this._time = _time;
       this._description = description;
       this._repeatingMinutes = _repeatingMinutes;

    }

    public Task(String name, String _status, String description, String dueDate, String _time) {
        this( name, _status,description, dueDate, _time, 0);

    }


    public Task(String name,  String _status, String description, String dueDate) {
        this( name,  _status,description, dueDate, "");
    }

    public Task(String name, String _status, String description) {
        this( name, _status,description, "");
    }

    public Task(String name,  String _status) {
        this( name,  _status,"");
    }

    public Task(String name) {
        this( name, "Active");
    }



    //Getters and Setters


    public String get_time() {
        return _time;
    }

    public long get_id() {
        return task_id;
    }



    public String get_name() {
        return _name;
    }

    public int get_repeatingMinutes() {
        return _repeatingMinutes;
    }

    public String get_description() {
        return _description;
    }

    public String get_dueDate() {
        return _dueDate;
    }

    public String get_status() {
        return _status;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_id(long id) {
        this.task_id = id;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public void set_dueDate(String _dueDate) {
        this._dueDate = _dueDate;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_repeatingMinutes(int _repeatingMinutes) {
        this._repeatingMinutes = _repeatingMinutes;
    }

    @Override
    public String toString() {
        return this._name;
    }


}
