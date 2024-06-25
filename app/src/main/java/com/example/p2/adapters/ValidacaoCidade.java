import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CidadeValidator {
    private Context context;

    public CidadeValidator(Context context) {
        this.context = context;
    }

    public void validarCidade(String cidade, CidadeValidationListener listener) {
        new ValidarCidadeTask(listener).execute(cidade);
    }

    private class ValidarCidadeTask extends AsyncTask<String, Void, Boolean> {
        private CidadeValidationListener listener;

        public ValidarCidadeTask(CidadeValidationListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String cidade = strings[0];
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(cidade, 1);
                return addresses != null && !addresses.isEmpty();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isValid) {
            if (listener != null) {
                listener.onValidationResult(isValid);
            }
        }
    }

    public interface CidadeValidationListener {
        void onValidationResult(boolean isValid);
    }
}
