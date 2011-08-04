package com.google.gwt.sample.contacts.server;

import com.google.gwt.sample.contacts.shared.ContactsService;
import com.google.gwt.sample.contacts.shared.Contact;
import com.google.gwt.sample.contacts.shared.ContactDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import javax.ejb.EJB;

@SuppressWarnings( "serial" )
public class ContactsServiceImpl
  extends RemoteServiceServlet
  implements ContactsService
{
  @EJB
  private LocalContacts _contacts;

  public Contact createOrUpdateContact( final Contact contact )
  {
    return _contacts.createOrUpdateContact( contact );
  }

  public void deleteContacts( final ArrayList<String> ids )
  {
    _contacts.deleteContacts( ids );
  }

  public ArrayList<ContactDetails> getContactDetails()
  {
    return _contacts.getContactDetails();
  }

  public Contact getContact( final String id )
  {
    return _contacts.getContact( id );
  }
}