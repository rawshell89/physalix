package hsa.awp.scire.procedureLogic.util;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "drawLog")
@XmlSeeAlso( {
        XmlDrawLog.LogEntry.class,
        XmlDrawLog.PrioList.class,
        XmlDrawLog.PrioListEntry.class,
        XmlDrawLog.Ticket.class
})
public class XmlDrawLog {

  @XmlElement(name = "logEntry")
  public List<LogEntry> logEntries = new ArrayList<LogEntry>();


  @XmlType
  public static class LogEntry {

    @XmlElement(name = "campaign")  public String campaign;
    @XmlElement(name = "procedure") public String procedure;
    @XmlElement(name = "fullname")  public String fullname;
    @XmlElement(name = "username")  public String username;
    @XmlElement(name = "mail")      public String mail;
    @XmlElement(name = "matnr")     public Integer matnr;

    @XmlElementWrapper(name = "priorityLists")
    @XmlElement(name = "priorityList")
    public List<PrioList> priorityLists = new ArrayList<PrioList>();

    @XmlElementWrapper(name = "tickets")
    @XmlElement(name = "ticket")
    public List<Ticket> tickets = new ArrayList<Ticket>();
  }

  @XmlType
  public static class Ticket {
    @XmlElement(name = "id")      public Long id;
    @XmlElement(name = "eventId") public Integer eventId;
    @XmlElement(name = "subject") public String subject;
    @XmlElement(name = "text")    public String text;
  }

  @XmlType
  public static class PrioList {

    @XmlElement(name = "nr") public Integer nr;

    @XmlElementWrapper(name = "priorityListEntries")
    @XmlElement(name = "priorityListEntry")
    public List<PrioListEntry> priorityListEntries = new ArrayList<PrioListEntry>();
  }

  @XmlType
  public static class PrioListEntry {
    @XmlElement(name = "id")       public Long id;
    @XmlElement(name = "priority") public Integer priority;
    @XmlElement(name = "eventId")  public Integer eventId;
    @XmlElement(name = "subject")  public String subject;
    @XmlElement(name = "text")     public String text;
  }
}
