/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database.Data;

/**
 *
 * @author STN-COM-01
 */
public class TemplateFinger {

    private int template_length;
    private int index_finger;

    public TemplateFinger(int template_length, int index_finger) {
        this.template_length = template_length;
        this.index_finger = index_finger;
    }

    /**
     * @return the template_length
     */
    public int getTemplate_length() {
        return template_length;
    }

    /**
     * @param template_length the template_length to set
     */
    public void setTemplate_length(int template_length) {
        this.template_length = template_length;
    }

    /**
     * @return the index_finger
     */
    public int getIndex_finger() {
        return index_finger;
    }

    /**
     * @param index_finger the index_finger to set
     */
    public void setIndex_finger(int index_finger) {
        this.index_finger = index_finger;
    }
}
