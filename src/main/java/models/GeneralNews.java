package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class GeneralNews {
    public int id;
    public String title;
    public String writtenBy;
    public String content;
    public int employee_id;
    private Date date= new Date();
    private Timestamp createdat;




    public GeneralNews(String title,String writtenBy, String content,int employee_id) {
        this.title = title;
        this.writtenBy = writtenBy;
        this.content = content;
        this.employee_id = employee_id;
        this.createdat = new Timestamp(date.getTime());


    }

    public String getTitle() {
        return title;
    }
    public String getWrittenBy() {
        return writtenBy;
    }
    public String getContent() {
        return content;
    }
    public int getId() {
        return id;
    }
    public int getEmployee_id() {
        return employee_id;
    }
    public Timestamp getCreatedat() { return createdat; }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setEmployee_id(int employee_id) { this.employee_id = employee_id; }


}
