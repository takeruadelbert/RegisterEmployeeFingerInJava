package Database.Data;

import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author STN-COM-01
 */
public class Employee {

    private int id;
    private String name;
    private String NIK;
    private List<TemplateFinger> template;

    public Employee(int id, String name, String NIK) {
        this.id = id;
        this.name = name;
        this.NIK = NIK;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the NIK
     */
    public String getNIK() {
        return NIK;
    }

    /**
     * @param NIK the NIK to set
     */
    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    /**
     * @return the template
     */
    public List<TemplateFinger> getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(List<TemplateFinger> template) {
        this.template = template;
    }
}
