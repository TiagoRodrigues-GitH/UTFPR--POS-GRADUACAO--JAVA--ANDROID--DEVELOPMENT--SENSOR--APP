package com.example.estudoemonitoramentodesensores.utils;

/**
 * Class: UtilsAlert
 * Project: Estudo e Monitoramento de Sensores
 *
 * Description:  This class provides static utility methods for displaying
 *               alert and confirmation dialogs throughout the application.
 *               This class cannot be instanciated.
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 10/03/2016
 */

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.estudoemonitoramentodesensores.R;

/**
 *
 *
 */
    public final class UtilsAlert {
    /**
     * Displays an alert dialog with a message from a string resource
     * and a listener for the neutral button.
     *
     * @param context    the context used to build the dialog
     * @param idMessagem the string resource ID of the message to display
     */

    public static void mostrarAviso(Context context,
                                    int idMessagem
                                    ) {
      mostrarAviso(context, context.getString(idMessagem), null);
    }

    /**
     * Displays an alert dialog with a custom message string
     * and a listener for the neutral button.
     *
     * @param context  the context used to build the dialog
     * @param messagem the message to display in the dialog
     * @param listener the listener triggered when the button is clicked
     */
    public static void mostrarAviso(Context context,
                                    String messagem,
                                    DialogInterface.OnClickListener listener) {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);

      builder.setTitle(R.string.aviso);
      builder.setIcon(R.drawable.ic_dialog_alert);
      builder.setMessage(messagem);
      builder.setNeutralButton(R.string.ok, listener);


      AlertDialog alertDialog = builder.create();
      alertDialog.show();
    }

    /**
     * Displays a confirmation dialog with a message from a string resource,
     * offering positive (yes) and negative (no) button options.
     *
     * @param context     the context used to build the dialog
     * @param idMensagem  the string resource ID of the message to display
     * @param listenerSim the listener triggered when the positive button is clicked
     * @param listenerNao the listener triggered when the negative button is clicked
     */
    public static void confirmarAcao(Context context,
                                     int idMensagem,
                                     DialogInterface.OnClickListener listenerSim,
                                     DialogInterface.OnClickListener listenerNao) {

      confirmarAcao(context, context.getString(idMensagem), listenerSim, listenerNao);
    }

    /**
     * Displays a confirmation dialog with a custom message string,
     * offering positive (yes) and negative (no) button options.
     *
     * @param context     the context used to build the dialog
     * @param mensagem    the message to display in the dialog
     * @param listenerSim the listener triggered when the positive button is clicked
     * @param listenerNao the listener triggered when the negative button is clicked
     */
    public static void confirmarAcao(Context context,
                                     String mensagem,
                                     DialogInterface.OnClickListener listenerSim,
                                     DialogInterface.OnClickListener listenerNao) {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);

      builder.setTitle(R.string.confirmacao);
      builder.setIcon(R.drawable.ic_dialog_alert);
      builder.setMessage(mensagem);
      builder.setPositiveButton(R.string.sim, listenerSim);
      builder.setNegativeButton(R.string.nao, listenerNao);

      AlertDialog alertDialog = builder.create();
      alertDialog.show();
    }

    public interface OnTextEnteredListener{
        void onTextEntered(String text);
    }

    public static void readText(Context context, int idTitulo, int idLayout, int idEditText,
                                String textoInicial, final OnTextEnteredListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(idTitulo);
        builder.setIcon(android.R.drawable.ic_input_get);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idLayout, null);

        final EditText editText = view.findViewById(idEditText);

        editText.setText(textoInicial);

        builder.setView(view);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String texto = editText.getText().toString();
                listener.onTextEntered(texto);
            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface dialog) {
                editText.requestFocus();
                editText.setSelection(editText.getText().toString().length());
            }
        });

        dialog.show();

    }




}
