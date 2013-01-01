package hsa.awp.scire.procedureLogic.util;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "phxlog")
@XmlSeeAlso( {
        XmlDrawLog.LogEntry.class,
        XmlDrawLog.PrioList.class,
        XmlDrawLog.PrioListEntry.class,
        XmlDrawLog.Ticket.class
})
public class XmlDrawLog {

  @XmlElement(name = "logentry")
  public List<LogEntry> logEntries = new ArrayList<LogEntry>();


  @XmlType
  public static class LogEntry {

    @XmlElement(name = "campaign")  public String campaign;
    @XmlElement(name = "procedure") public String procedure;
    @XmlElement(name = "fullname")  public String fullname;
    @XmlElement(name = "username")  public String username;
    @XmlElement(name = "mail")      public String mail;
    @XmlElement(name = "matnr")     public Integer matnr;

    @XmlElementWrapper(name = "lists")
    @XmlElement(name = "list")
    public List<PrioList> lists = new ArrayList<PrioList>();

    @XmlElementWrapper(name = "tickets")
    @XmlElement(name = "ticket")
    public List<Ticket> tickets = new ArrayList<Ticket>();
  }

  @XmlType
  public static class Ticket {
    @XmlElement(name = "eventid") public Integer eventid;
    @XmlElement(name = "course")  public String course;
    @XmlElement(name = "text")    public String text;
  }

  @XmlType
  public static class PrioList {

    @XmlElement(name = "nr") public Integer nr;

    @XmlElementWrapper(name = "listentries")
    @XmlElement(name = "listentry")
    public List<PrioListEntry> listentries = new ArrayList<PrioListEntry>();
  }

  @XmlType
  public static class PrioListEntry {
    @XmlElement(name = "prio")    public Integer prio;
    @XmlElement(name = "eventid") public Integer eventid;
    @XmlElement(name = "course")  public String course;
    @XmlElement(name = "text")    public String text;
  }
}
