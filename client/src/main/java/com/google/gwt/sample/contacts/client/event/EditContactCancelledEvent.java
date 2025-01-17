package com.google.gwt.sample.contacts.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditContactCancelledEvent
  extends GwtEvent<EditContactCancelledEventHandler>
{
  public static final Type<EditContactCancelledEventHandler> TYPE = new Type<EditContactCancelledEventHandler>();

  @Override
  public Type<EditContactCancelledEventHandler> getAssociatedType()
  {
    return TYPE;
  }

  @Override
  protected void dispatch( final EditContactCancelledEventHandler handler )
  {
    handler.onEditContactCancelled( this );
  }
}
