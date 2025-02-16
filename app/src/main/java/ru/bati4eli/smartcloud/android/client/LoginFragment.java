package ru.bati4eli.smartcloud.android.client;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import ru.bati4eli.smartcloud.android.client.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.loginButton.setOnClickListener(v -> onLoginButtonClicked());

        return binding.getRoot();
    }

    private void onLoginButtonClicked() {
        String username = binding.usernameEntry.getText().toString();
        String password = binding.passwordEntry.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            setLabel("Пожалуйста, введите имя пользователя и пароль.", "#0000FF");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        setLabel("", null);

//        try {
//            JwtRequest jwtRequest = JwtRequest.newBuilder()
//                    .setLogin(username)
//                    .setPass(password)
//                    .build();
//
//            JwtResponse response = authClient.authenticate(jwtRequest);
        setLabel("Успешная авторизация!", "#008000");
        NavHostFragment.findNavController(this).navigate(R.id.action_from_login_to_settings);
//            ParametersUtil.SetSecrets(username, password);
//            ParametersUtil.SetToken(response.getToken());

//            if (ParametersUtil.NeedSetupPage()) {
//                // Переход к следующему экрану
//                startActivity(new Intent(this, StartSettingsActivity.class));
//            } else {
//                startActivity(new Intent(this, FilesActivity.class));
//            }

//        } catch (StatusRuntimeException e) {
//            setLabel(e.getStatus().getDescription(), "#FF0000");
//        } finally {
//            loadingIndicator.setVisibility(View.GONE);
//        }
    }

    private void setLabel(String message, String color) {
        binding.messageLabel.setText(message);
        if (color != null) {
            binding.messageLabel.setTextColor(Color.parseColor(color));
        }
    }

    private void loadCredentials() {
//        String username = ParametersUtil.GetUsername();
//        String password = ParametersUtil.GetPassword();

//        if (username != null) {
//            usernameEntry.setText(username);
//        }
//
//        if (password != null) {
//            passwordEntry.setText(password);
//        }
    }

}



