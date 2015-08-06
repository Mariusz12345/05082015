package test.GwtMeaven.client;

import test.GwtMeaven.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtMeaven implements EntryPoint {
	
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  /**
   * Create a 	remote service proxy to talk to the server-side Greeting service.
   */
  private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

  private final Messages messages = GWT.create(Messages.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final Button sendButton = new Button( messages.sendButton() );
    final TextBox nameField = new TextBox();
    final TextBox nameField2 = new TextBox();
    final Button wyczysci = new Button(messages.Button());
    nameField.setText( messages.nameField() );
    
    nameField2.setText(messages.nameField1());
    
    final Label errorLabel = new Label();
    final Button button = new Button(messages.Button());

    // We can add style names to widgets
    sendButton.addStyleName("sendButton");
    button.addStyleName("name");
    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("sendButtonContainer").add(sendButton);
    RootPanel.get("errorLabelContainer").add(errorLabel);
    RootPanel.get("nameFieldContainer").add(nameField2);
    RootPanel.get("sendButtonContainer").add(wyczysci);

    // Focus the cursor on the name field when the app loads
    nameField2.setFocus(true);
    nameField2.selectAll();
    
    nameField.setFocus(true);
    nameField.selectAll();

    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Faktura");
    dialogBox.setAnimationEnabled(true);
    final Button closeButton = new Button("Zamknij");
    final DialogBox dialogBox2 = new DialogBox();
    // We can set the id of a widget by accessing its Element
    closeButton.getElement().setId("closeButton");
    wyczysci.getElement().setId("wyczysci");
    
    dialogBox2.setText("nowe okienko");
    dialogBox2.setAnimationEnabled(true);
    
    
    final Label textToServerLabel = new Label();
    final Label textToServerlabels = new Label();
    
    final HTML serverResponseLabel = new HTML();
    VerticalPanel dialogVPanel = new VerticalPanel();
    
    VerticalPanel dialogpanel2 = new VerticalPanel();
  
   
    
    dialogVPanel.addStyleName("dialogVPanel");
    dialogVPanel.add(new HTML("<b>Dane faktury</b>"));
    dialogVPanel.add(textToServerLabel);
    dialogVPanel.add(textToServerlabels);
    dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
    dialogVPanel.add(serverResponseLabel);
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);
    
    dialogBox2.setWidget(dialogpanel2);
    // Add a handler to close the DialogBox
    wyczysci.addClickHandler(new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			nameField.setText("");
			nameField2.setText("");
		}
	});
    
   
    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
       
        dialogBox.hide();
        
        sendButton.setEnabled(true);
        sendButton.setFocus(true);
       
      }
    });

    // Create a handler for the sendButton and nameField
    class MyHandler implements ClickHandler, KeyUpHandler {
      /**
       * Fired when the user clicks on the sendButton.
       */
      public void onClick(ClickEvent event) {
        sendNameToServer();
      }

      /**
       * Fired when the user types in the nameField.
       */
      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          sendNameToServer();
        }
      }

      /**
       * Send the name from the nameField to the server and wait for a response.
       */
      private void sendNameToServer() {
        // First, we validate the input.
        errorLabel.setText("");
        String textToServer = nameField.getText();
        if (!FieldVerifier.isValidName(textToServer)) {
          errorLabel.setText("Please enter at least four characters");
          return;
        }
        errorLabel.setText("");
        String textToServer2= nameField2.getText();
        if (!FieldVerifier.isValidName(textToServer2)) {
          errorLabel.setText("Please enter at least four characters");
          return;
        }

        // Then, we send the input to the server.
        sendButton.setEnabled(false);
        textToServerLabel.setText(textToServer);
        textToServerlabels.setText(textToServer2);
        serverResponseLabel.setText("sssssss");
        greetingService.greetServer(textToServer, new AsyncCallback<String>() {
          public void onFailure(Throwable caught) {
            // Show the RPC error message to the user
            dialogBox.setText("Remote Procedure Call - Failure");
            serverResponseLabel.addStyleName("serverResponseLabelError");
            serverResponseLabel.setHTML(SERVER_ERROR);
            dialogBox.center();
            closeButton.setFocus(true);
          }

          public void onSuccess(String result) {
            dialogBox.setText("Faktura");
            serverResponseLabel.removeStyleName("serverResponseLabelError");
            serverResponseLabel.setHTML(result);
            dialogBox.center();
            closeButton.setFocus(true);
          }
        });
      }
    }

    // Add a handler to send the name to the server
    MyHandler handler = new MyHandler();
    sendButton.addClickHandler(handler);
    nameField.addKeyUpHandler(handler);
    nameField2.addKeyUpHandler(handler);
  }
}
