package com.edwingonzalez.Funciones;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.PopupWindow;
import android.app.Activity;
import android.app.AlertDialog;
import android.widget.ImageButton;
import java.util.Objects;

import com.edwingonzalez.apiplaces.R;

import java.text.DecimalFormat;

public class Utilidades {
    private static Toast toastMostrado;

    //-----------------------Vacio----------------------------\\
    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    //-----------------------Error en el editText----------------------------\\
    public static void setError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    //-----------------------decimalFormat----------------------------\\
    public static String formatearNumero(double numero, int decimales) {
        StringBuilder patron = new StringBuilder("#,##0");
        if (decimales > 0) {
            patron.append(".");
            for (int i = 0; i < decimales; i++) {
                patron.append("#");
            }
        }
        DecimalFormat formato = new DecimalFormat(patron.toString());
        return formato.format(numero);
    }

    //-----------------------Limpiar editText----------------------------\\
    public static void borrarDatos(EditText[] editTexts, EditText editTextFoco) {
        for (EditText editText : editTexts) {
            editText.setText("");
        }

        if (editTextFoco != null) {
            editTextFoco.requestFocus();
        }
    }

    //-----------------------Toast----------------------------\\
    public static void mostrarToast(Context context, String mensaje, int iconResId) {
        if (toastMostrado != null) {
            toastMostrado.cancel();
        }

        // Inflar el diseño personalizado
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        // Obtener las referencias a los elementos del diseño
        ImageView toastIcon = layout.findViewById(R.id.toast_icon);
        TextView toastMessage = layout.findViewById(R.id.toast_message);

        // Configurar el mensaje del toast
        toastMessage.setText(mensaje);

        // Configurar la imagen del toast
        toastIcon.setImageResource(iconResId);

        // Crear el toast y establecer el diseño personalizado
        toastMostrado = new Toast(context);
        toastMostrado.setDuration(Toast.LENGTH_SHORT);
        toastMostrado.setView(layout);
        toastMostrado.show();
    }

    //-----------------------Tooltip----------------------------\\
    public static PopupWindow mostrarTooltip(Context context, View anchorView, String message) {
        // Inflar el diseño del tooltip
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tooltipView = inflater.inflate(R.layout.tooltip_layout, null);

        // Configurar el texto del tooltip
        TextView tooltipText = tooltipView.findViewById(R.id.tooltip_text);
        tooltipText.setText(message);

        // Crear el PopupWindow
        final PopupWindow popupWindow = new PopupWindow(tooltipView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Mostrar el PopupWindow
        popupWindow.showAsDropDown(anchorView, 0, 10, Gravity.CENTER_HORIZONTAL);

        return popupWindow;
    }

    public static void setTooltip(final Context context, final View view, final String message) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupWindow tooltipWindow = Utilidades.mostrarTooltip(context, v, message);
                v.setTag(tooltipWindow);
                return true;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    PopupWindow tooltipWindow = (PopupWindow) v.getTag();
                    if (tooltipWindow != null && tooltipWindow.isShowing()) {
                        tooltipWindow.dismiss();
                        v.setTag(null);
                    }
                }
                return false;
            }
        });
    }

    //-----------------------Dialog de salida----------------------------\\
    public static void mostrarDialogoSalida(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);

        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        ImageButton imageButtonCancelar = dialogView.findViewById(R.id.imageButtonCancelar);
        ImageButton imageButtonAceptar = dialogView.findViewById(R.id.imageButtonAceptar);

        imageButtonCancelar.setOnClickListener(v -> alertDialog.dismiss());
        imageButtonAceptar.setOnClickListener(view -> activity.finishAffinity());

        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    //-----------------------Quitar teclado----------------------------\\
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //-----------------------Limitar Números----------------------------\\
    public static void decimalesCorte(EditText editText, int numDecimales, double min, double max) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                // Verificar si el texto está dentro del rango permitido
                if (!input.isEmpty()) {
                    try {
                        double number = Double.parseDouble(input);
                        if (number < min || number > max) {
                            // Si el número está fuera del rango, ajustar al valor más cercano permitido
                            if (number < min) {
                                editText.setText(String.valueOf(min));
                            } else {
                                editText.setText(String.valueOf(max));
                            }
                            editText.setSelection(editText.getText().length());
                            return;
                        }
                    } catch (NumberFormatException e) {
                        
                    }
                }

                // Verificar si el número de decimales excede el límite
                if (input.contains(".")) {
                    String[] parts = input.split("\\.");
                    if (parts.length > 1 && parts[1].length() > numDecimales) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
            }
        });
    }
}
