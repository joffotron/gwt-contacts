package com.google.gwt.sample.contacts.client.presenter;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.sample.contacts.client.Presenter;
import com.google.gwt.sample.contacts.client.common.SelectionModel;
import com.google.gwt.sample.contacts.client.event.AddContactEvent;
import com.google.gwt.sample.contacts.client.event.EditContactEvent;
import com.google.gwt.sample.contacts.client.view.ContactsView;
import com.google.gwt.sample.contacts.client.view.ContactsViewUI;
import com.google.gwt.sample.contacts.shared.ContactDetails;
import com.google.gwt.sample.contacts.shared.ContactsServiceAsync;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import java.util.ArrayList;
import java.util.List;

public class ContactsPresenter
  extends AbstractActivity
  implements Presenter, ContactsView.Presenter
{
  private final ContactsServiceAsync _rpcService;
  private final EventBus _eventBus;
  private final SelectionModel<ContactDetails> _selectionModel;

  private ContactsView _view;
  private List<ContactDetails> _contactDetails;

  public ContactsPresenter( final ContactsServiceAsync rpcService, final EventBus eventBus )
  {
    _rpcService = rpcService;
    _eventBus = eventBus;
    _selectionModel = new SelectionModel<ContactDetails>();
  }

  @Override
  public void start( final AcceptsOneWidget panel, final EventBus eventBus )
  {
    final ContactsView view = new ContactsViewUI();
    view.setPresenter( this );
    panel.setWidget( view.asWidget() );
    _view = view;
    fetchContactDetails( view );
  }

  public void onAddButtonClicked()
  {
    _eventBus.fireEvent( new AddContactEvent() );
  }

  public void onDeleteButtonClicked()
  {
    deleteSelectedContacts();
  }

  public void onItemClicked( final ContactDetails contactDetails )
  {
    _eventBus.fireEvent( new EditContactEvent( contactDetails.getId() ) );
  }

  public void onItemSelected( final ContactDetails contactDetails )
  {
    if ( _selectionModel.isSelected( contactDetails ) )
    {
      _selectionModel.removeSelection( contactDetails );
    }
    else
    {
      _selectionModel.addSelection( contactDetails );
    }
  }

  public void sortContactDetails()
  {
    // Yes, we could use a more optimized method of sorting, but the
    //  point is to create a test case that helps illustrate the higher
    //  level concepts used when creating MVP-based applications.
    //
    final int size = _contactDetails.size();
    for ( int i = 0; i < size; ++i )
    {
      for ( int j = 0; j < size - 1; ++j )
      {
        if ( _contactDetails.get( j )
               .getDisplayName()
               .compareToIgnoreCase( _contactDetails.get( j + 1 ).getDisplayName() ) >= 0 )
        {
          final ContactDetails tmp = _contactDetails.get( j );
          _contactDetails.set( j, _contactDetails.get( j + 1 ) );
          _contactDetails.set( j + 1, tmp );
        }
      }
    }
  }

  public void setContactDetails( final List<ContactDetails> contactDetails )
  {
    this._contactDetails = contactDetails;
    sortContactDetails();
  }

  public List<ContactDetails> getContactDetails()
  {
    return _contactDetails;
  }

  private void fetchContactDetails( final ContactsView view )
  {
    _rpcService.getContactDetails( new AsyncCallback<ArrayList<ContactDetails>>()
    {
      public void onSuccess( final ArrayList<ContactDetails> result )
      {
        setContactDetails( result );
        view.setRowData( _contactDetails );
      }

      public void onFailure( final Throwable caught )
      {
        Window.alert( "Error fetching contact details" );
      }
    } );
  }

  private void deleteSelectedContacts()
  {
    final List<ContactDetails> selectedContacts = _selectionModel.getSelectedItems();
    final ArrayList<String> ids = new ArrayList<String>();

    for ( final ContactDetails selected : selectedContacts )
    {
      ids.add( selected.getId() );
    }

    _rpcService.deleteContacts( ids, new AsyncCallback<Void>()
    {
      public void onSuccess( final Void result )
      {
        fetchContactDetails( _view );
        _selectionModel.clear();
      }

      public void onFailure( final Throwable caught )
      {
        System.out.println( "Error deleting selected contacts" );
      }
    } );
  }
}
