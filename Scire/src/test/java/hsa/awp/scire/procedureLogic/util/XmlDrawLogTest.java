package hsa.awp.scire.procedureLogic.util;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class XmlDrawLogTest {

  @Test
  public void testJAXBContextInitialization() throws JAXBException {
    JAXBContext.newInstance(XmlDrawLog.class);
  }
}
