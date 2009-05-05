/**
 * 
 */
package org.mnode.touchbase.im.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.action.ActionManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.mnode.touchbase.im.XMPPConnectionManager;

/**
 * @author Ben
 *
 */
public class SendFile extends AbstractContactAction {

    private XMPPConnectionManager contactStore;
    
    private JFileChooser sendFileChooser;
    
    private Component parent;
    
    private FileTransferManager ftm;
    
    /**
     * 
     */
    private static final long serialVersionUID = -653913819015809588L;

    /**
     * 
     */
    public SendFile(String id) {
        super("Send File..", id, null);
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_DOWN_MASK));
        sendFileChooser = new JFileChooser();
        ActionManager.getInstance().addAction(this);
    }

    /**
     * @return the contactStore
     */
    public final XMPPConnectionManager getContactStore() {
        return contactStore;
    }

    /**
     * @param contactStore the contactStore to set
     */
    public final void setContactStore(XMPPConnectionManager contactStore) {
        this.contactStore = contactStore;
        setEnabled(contactStore != null && super.isEnabled());
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (sendFileChooser.showDialog(parent, "Send") == JFileChooser.APPROVE_OPTION) {
            
            SwingWorker<String, Object> sendFileWorker = new SwingWorker<String, Object>() {
                @Override
                protected String doInBackground() throws Exception {
                    File selected = sendFileChooser.getSelectedFile();
                    ProgressMonitor monitor = new ProgressMonitor(parent, "Transferring file [" + selected.getName() + "]", null, 0, 100);
                    XMPPConnection connection = contactStore.getConnection(getContact());
                    if (connection != null) {
                        FileTransferManager ftm = new FileTransferManager(connection);
                        OutgoingFileTransfer oft = ftm.createOutgoingFileTransfer(getContact());
                        
                        try {
                            oft.sendFile(selected, selected.getName());
                        } catch (XMPPException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        
                        while (!oft.isDone()) {
                            if (monitor.isCanceled()) {
                                oft.cancel();
                            }
                            monitor.setNote(oft.getStatus().name());
                            monitor.setProgress((int) (100 * oft.getProgress()));
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                    }
                    monitor.setProgress(0);
                    return null;
                }
            };
            sendFileWorker.execute();
        }
    }

    /**
     * @return the parent
     */
    public final Component getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public final void setParent(Component parent) {
        this.parent = parent;
    }

}
