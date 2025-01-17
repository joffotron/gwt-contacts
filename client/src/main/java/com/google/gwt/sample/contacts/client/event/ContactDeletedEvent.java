package com.google.gwt.sample.contacts.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ContactDeletedEvent
  extends GwtEvent<ContactDeletedEventHandler>
{
  public static final Type<ContactDeletedEventHandler> TYPE = new Type<ContactDeletedEventHandler>();

  @Override
  public Type<ContactDeletedEventHandler> getAssociatedType()
  {
    return TYPE;
  }

  @Override
  protected void dispatch( final ContactDeletedEventHandler handler )
  {
    handler.onContactDeleted( this );
  }
}
