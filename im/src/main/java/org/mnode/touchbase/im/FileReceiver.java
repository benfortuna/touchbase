package org.mnode.touchbase.im;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

/**
 * Manages receipt of transferred files.
 * 
 * @author fortuna
 * 
 */
public class FileReceiver implements FileTransferListener {

    private JFileChooser acceptFileChooser;

    private Component parent;

    /**
     * 
     */
    public FileReceiver() {
        acceptFileChooser = new JFileChooser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileTransferRequest(final FileTransferRequest request) {
        if (JOptionPane.showConfirmDialog(parent, "Accept file from " + request.getRequestor() + "?",
                "Incoming File Transfer", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            acceptFileChooser.setSelectedFile(new File(acceptFileChooser.getCurrentDirectory(), request.getFileName()));
            if (acceptFileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                SwingWorker<String, Object> acceptFileWorker = new SwingWorker<String, Object>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        ProgressMonitor monitor = new ProgressMonitor(parent, "Receiving file ["
                                + request.getFileName() + "]", null, 0, 100);
                        IncomingFileTransfer ift = request.accept();
                        try {
                            ift.recieveFile(acceptFileChooser.getSelectedFile());
                        } catch (XMPPException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        while (!ift.isDone()) {
                            if (monitor.isCanceled()) {
                                ift.cancel();
                            }
                            monitor.setNote(ift.getStatus().name());
                            monitor.setProgress((int) (100 * ift.getProgress()));
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                        monitor.setProgress(0);
                        return null;
                    }
                };
                acceptFileWorker.execute();
            }
        }
    }

    /**
     * @return the parent
     */
    public final Component getParent() {
        return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public final void setParent(Component parent) {
        this.parent = parent;
    }
}
