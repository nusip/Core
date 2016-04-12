package kz.maks.core.front.ui;

import kz.maks.core.front.services.Callback;
import kz.maks.core.front.services.asyncs.ICRUDAsync;
import kz.maks.core.shared.dtos.AbstractSearchParams;
import kz.maks.core.shared.models.HasId;
import kz.maks.core.shared.models.ListResponse;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static kz.maks.core.shared.Utils.extractId;

public class CRUD<PARAMS extends AbstractSearchParams, RECORD extends HasId, DETAILS> {
    public static final int PAGE_SIZE = 100;
    private final JFrame rootFrame;
    private final Table<RECORD> table;
    private final BtnPgPanel btnPgPanel;
    private final BtnCRUDPanel btnCRUDPanel;
    private final ICRUDAsync<PARAMS, RECORD, DETAILS> crudAsync;
    private PARAMS lastParams;
    private boolean hasNextPage;

    public CRUD(final JFrame rootFrame,
                final SearchPanel<PARAMS> searchPanel,
                final Table<RECORD> table,
                final BtnPgPanel btnPgPanel,
                final BtnCRUDPanel btnCRUDPanel,
                final FormDialog<DETAILS> formDialog,
                final ICRUDAsync<PARAMS, RECORD, DETAILS> crudAsync) {
        this.rootFrame = rootFrame;
        this.table = table;
        this.btnPgPanel = btnPgPanel;
        this.btnCRUDPanel = btnCRUDPanel;
        this.crudAsync = crudAsync;

        resetEditDeleteButtons();
        resetPgButtons();

        table.ui.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                resetEditDeleteButtons();
                // TODO check
//                System.out.println("selection");
            }
        });

        searchPanel.btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastParams = searchPanel.form.get();
                lastParams.setPage(1);
                search();
            }
        });


        btnCRUDPanel.btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formDialog.form.set(null);
                formDialog.ui.setVisible(true);
            }
        });

        btnCRUDPanel.btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crudAsync.get(extractId(table.getSelected()), new ProgressDialogCallback<DETAILS>(rootFrame) {
                    @Override
                    public void onSuccess(DETAILS details) {
                        formDialog.form.set(details);
                        formDialog.ui.setVisible(true);
                    }
                });
            }
        });

        btnCRUDPanel.btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crudAsync.delete(extractId(table.getSelected()), new Callback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        table.removeSelected();
                    }
                });
            }
        });

        formDialog.runOnValid(new Runnable() {
            @Override
            public void run() {
                crudAsync.save(formDialog.form.get(), new ProgressDialogCallback<Void>(rootFrame) {
                    @Override
                    public void onSuccess(Void aVoid) {
                        formDialog.ui.setVisible(false);
                    }
                });
            }
        });

        btnPgPanel.btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastParams.setPage(lastParams.getPage() + 1);
                search();
            }
        });

        btnPgPanel.btnPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastParams.setPage(lastParams.getPage() - 1);
                search();
            }
        });

    }

    private void resetPgButtons() {
        btnPgPanel.btnPrev.setEnabled(lastParams != null ? lastParams.getPage() > 1 : false);
        btnPgPanel.btnNext.setEnabled(hasNextPage);
    }

    private void resetEditDeleteButtons() {
        boolean isSelected = table.getSelected() != null;
        btnCRUDPanel.btnEdit.setEnabled(isSelected);
        btnCRUDPanel.btnDelete.setEnabled(isSelected);
    }

    private void search() {
        lastParams.setPageSize(PAGE_SIZE);

        crudAsync.list(lastParams, new ProgressDialogCallback<ListResponse<RECORD>>(rootFrame) {
            @Override
            public void onSuccess(ListResponse<RECORD> listResponse) {
                table.set(listResponse.getList());
                hasNextPage = listResponse.isHasNext();
                resetPgButtons();
            }
        });
    }

}
