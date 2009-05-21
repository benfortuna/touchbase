/**
 * 
 */
package org.mnode.touchbase.im;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.action.ActionManager;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.PipelineEvent;
import org.jdesktop.swingx.decorator.PipelineListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smackx.packet.VCard;
import org.mnode.base.views.AbstractView;
import org.mnode.touchbase.im.action.CallContact;
import org.mnode.touchbase.im.action.OpenChatView;
import org.mnode.touchbase.im.action.SendFile;

/**
 * @author fortuna
 *
 */
public class ContactsView extends AbstractView {

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("org.mnode.touchbase.im.messages");

    private static enum Type {CONTACTS, GROUPS, RESOURCES};
    
    private PatternFilter findFilter;

	private JTextField findField;
	
	private JXList contactList;
	
	private XMPPConnectionManager contactStore;
	
	private Type type;
	
	/**
	 * 
	 */
	public ContactsView(final XMPPConnectionManager contactStore) {
		super("contacts", "Contacts");
	
		this.contactStore = contactStore;
		
		findFilter = new PatternFilter();
		
		Box findBox = Box.createHorizontalBox();
        findBox.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));

		findField = new JTextField();
        findField.setForeground(Color.LIGHT_GRAY);
        findField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (findField.getText().equals(MESSAGES.getString("label.find" + type.name()))) {
                    findField.setText(null);
                }
            }
            public void focusLost(FocusEvent e) {
                if (findField.getText().length() == 0) {
                    resetFindField();
                }
            }
        });
		findField.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		            findField.setText("");
		        }
		    }
		});
		findField.getDocument().addDocumentListener(new DocumentListener() {
		    public void removeUpdate(DocumentEvent e) {
                resetFilter();
		    }
		    public void insertUpdate(DocumentEvent e) {
                resetFilter();
		    }
		    public void changedUpdate(DocumentEvent e) {
		        resetFilter();
		    }
		    private void resetFilter() {
		        if (findField.getText().length() > 0
		                && !findField.getText().equals(MESSAGES.getString("label.find" + type.name()))) {
		            String pattern = "\\Q" + findField.getText() + "\\E";
		            findFilter.setPattern(pattern, Pattern.CASE_INSENSITIVE);
		        }
		        else {
		            findFilter.setPattern(null);
		        }
		    }
		});
		findBox.add(findField);
		
		/*
		JXButton clearFindButton = new JXButton("X");
		clearFindButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        findField.setText("");
		    }
		});
		findBox.add(clearFindButton);
		*/

		final JPopupMenu changeTypeMenu = new JPopupMenu("Change Type");
        changeTypeMenu.add(new AbstractAction("Contacts") {
            public void actionPerformed(ActionEvent e) {
                setType(Type.CONTACTS);
                findField.requestFocus();
            }
        });
        changeTypeMenu.add(new AbstractAction("Groups") {
            public void actionPerformed(ActionEvent e) {
                setType(Type.GROUPS);
                findField.requestFocus();
            }
        });
        changeTypeMenu.add(new AbstractAction("Resources") {
            public void actionPerformed(ActionEvent e) {
                setType(Type.RESOURCES);
                findField.requestFocus();
            }
        });
        // XXX: need to ensure the size is correct so we know where to display the popup..
//        changeTypeMenu.pack();
		
		final JXButton changeTypeButton = new JXButton("V");
		changeTypeButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        changeTypeMenu.show(changeTypeButton, changeTypeButton.getWidth() - changeTypeMenu.getWidth(), changeTypeButton.getHeight());
		    }
		});
		findBox.add(changeTypeButton);
		add(findBox, BorderLayout.NORTH);
		
		contactList = new JXList(true) {
			@Override
			public JToolTip createToolTip() {
			    if (getMousePosition() != null) {
	                final VCard contact = (VCard) getElementAt(locationToIndex(getMousePosition()));
	                if (contact.getAvatar() != null) {
	                	final ImageIcon avatar = new ImageIcon(contact.getAvatar());
	                    final int width = avatar.getIconWidth();
	                    final int height = avatar.getIconHeight();
	                    
	                    JToolTip toolTip = new JToolTip() {
	                        @Override
	                        public void paint(Graphics g) {
	                            Graphics2D g2d = (Graphics2D) g;
	                            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	                            g2d.setColor(getBackground());
	                            
	                            g2d.fillRect(0, 0, width, height);

	                            paintBorder(g2d);

	                            Insets insets = getInsets();
	                            g2d.drawImage(avatar.getImage(), insets.left, insets.top, width - (insets.left + insets.right),
	                                    height - (insets.top + insets.bottom), null);
	                        }
	                    };
	                    toolTip.setPreferredSize(new Dimension(width, height));
	                    toolTip.setBorder(BorderFactory.createLineBorder(UIManager.getColor("PopupMenu.border")));
	                    return toolTip;
	                }
			    }
				return super.createToolTip();
			}
		};
		contactList.setForeground(Color.WHITE);
		contactList.setFont(contactList.getFont().deriveFont(12f));
		
		FilterPipeline filterPipeline = new FilterPipeline(findFilter);
        filterPipeline.addPipelineListener(new PipelineListener() {
            @Override
            public void contentsChanged(PipelineEvent e) {
            	contactList.clearSelection();
                setStatusMessage(contactList.getElementCount() + " contact(s)");
            }
        });
		
		contactList.setFilters(filterPipeline);
//		contactList.addHighlighter(AlternateRowHighlighter.quickSilver);
//		contactList.addHighlighter(new RolloverHighlighter(Color.BLACK, Color.ORANGE));
//		contactList.setCellRenderer(new DefaultListCellRenderer() {
//		    @Override
//		    public Component getListCellRendererComponent(JList list,
//		            Object value, int index, boolean isSelected,
//		            boolean cellHasFocus) {
//
//		        Contact contact = (Contact) value;
//		        if (contact.getPresence() != null) {
//		        }
//		        else {
//		            setFont(getFont().deriveFont(Font.PLAIN));
//		        }
//		        
//		        return super.getListCellRendererComponent(list, value, index, isSelected,
//		                cellHasFocus);
//		    }
//		});
		contactList.setCellRenderer(new ContactListCellRenderer(contactStore));
//		contactList.setAutoscrolls(true);
//		contactList.addMouseMotionListener(new MouseMotionAdapter() {
//		    @Override
//		    public void mouseDragged(MouseEvent e) {
//		        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
//		        contactList.scrollRectToVisible(r);
//		    }
//		});
        contactList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	@Override
        	public void valueChanged(ListSelectionEvent e) {
        		if (!e.getValueIsAdjusting()) {
                    OpenChatView openChat = (OpenChatView) ActionManager.getInstance().getAction("chat");
                    CallContact callContact = (CallContact) ActionManager.getInstance().getAction("call");
                    SendFile sendFile = (SendFile) ActionManager.getInstance().getAction("sendFile");
        			if (contactList.getSelectedValues().length > 0) {
        			    openChat.setContact(((RosterEntry) contactList.getSelectedValues()[0]).getUser()); 
                        callContact.setContact(((RosterEntry) contactList.getSelectedValues()[0]).getUser()); 
                        sendFile.setContact(((RosterEntry) contactList.getSelectedValues()[0]).getUser()); 
            			setStatusMessage(contactList.getSelectedValues().length + " contact(s) selected.");
        			}
        			else {
                        openChat.setContact(null); 
                        callContact.setContact(null); 
                        sendFile.setContact(null); 
						setStatusMessage(contactList.getElementCount() + " contact(s)");
        			}
        		}
        	}
        });
		
		contactList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int viewIndex = contactList.locationToIndex(e.getPoint());
                    if (viewIndex >= 0) {
                        int index = contactList.convertIndexToModel(viewIndex);
                        OpenChatView openChat = (OpenChatView) ActionManager.getInstance().getAction("chat");
//                        openChat.getChatFrame().openChat((RosterEntry) contactList.getModel().getElementAt(index));
                    }
                }
            }
		});

		List<Action> listActions = new ArrayList<Action>();
        listActions.add(ActionManager.getInstance().getAction("contactList.details"));
		listActions.add(ActionManager.getInstance().getAction("contactList.call"));
//        listActions.add(new OpenChatView(null, this));
		
		JXLayer<JXList> contactListLayer = new JXLayer<JXList>(contactList);
		contactListLayer.setUI(new ListActionsUI(listActions));
		
//		JScrollPane contactScroller = new JScrollPane(contactListLayer, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
//                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
////		contactScroller.setWheelScrollingEnabled(true);
//        contactScroller.setBorder(BorderFactory.createEmptyBorder());
//        contactScroller.setViewportBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
//        
//        JXLayer<JScrollPane> contactsLayer = new JXLayer<JScrollPane>(contactScroller, new MouseScrollableUI());
        
		add(contactListLayer, BorderLayout.CENTER);

		setType(Type.CONTACTS);
	}

	/**
	 * @param type
	 */
	private void setType(Type type) {
	    this.type = type;
	    if (Type.CONTACTS.equals(type)) {
	        ListModel model = new ContactsListModel(contactStore);
	        model.addListDataListener(new ListDataListener() {
	            @Override
	            public void contentsChanged(ListDataEvent e) {
	                if (e.getIndex0() < contactList.getSelectedIndex() && e.getIndex1() > contactList.getSelectedIndex()) {
	                    contactList.clearSelection();
	                }
	            }
	            @Override
	            public void intervalAdded(ListDataEvent e) {
	                contactList.clearSelection();
	            }
	            @Override
	            public void intervalRemoved(ListDataEvent e) {
	                contactList.clearSelection();
	            }
	        });
	        contactList.setModel(model);
	    }
	    else if (Type.GROUPS.equals(type)) {
            contactList.setModel(new GroupsListModel(contactStore));
        }
	    resetFindField();
	}
	
    /**
     * Resets the find field.
     */
    private void resetFindField() {
        findField.setText(MESSAGES.getString("label.find" + type.name()));
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JXFrame f = new JXFrame(ContactsView.class.getSimpleName());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new ContactsView(new XMPPConnectionManager()));
		f.pack();
        f.setStartPosition(StartPosition.CenterInScreen);
		f.setVisible(true);
	}

    /**
     * @return the contactList
     */
    public final JXList getContactList() {
        return contactList;
    }
}
