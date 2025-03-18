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
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.SneakyThrows;
import lombok.var;
import ru.bati4eli.mycloud.users.UserPrivateServiceGrpc;
import ru.bati4eli.mycloud.users.UserService;
import ru.bati4eli.smartcloud.android.client.databinding.FragmentLoginBinding;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private UserPrivateServiceGrpc.UserPrivateServiceBlockingStub authClient;
    private ManagedChannel channel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ParametersUtil.setContext(requireContext());

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        try {
            channel = ManagedChannelBuilder.forAddress("bati4eli.ru", 9090)
                    .usePlaintext()
                    .build();

            authClient = UserPrivateServiceGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            setLabel(e.getMessage(), Color.RED);
        }

        binding.loginButton.setOnClickListener(v -> onLoginButtonClicked());
        loadCredentials();
        return binding.getRoot();
    }

    @SneakyThrows
    private void onLoginButtonClicked() {
        String username = binding.usernameEntry.getText().toString();
        String password = binding.passwordEntry.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            setLabel("Пожалуйста, введите имя пользователя и пароль.", Color.BLUE);
            return;
        }

        binding.loadingIndicator.setVisibility(View.VISIBLE);
        setLabel("", Color.BLUE);

        try {
            UserService.JwtResponse response = authorize(username, password);
            setLabel("Успешная авторизация!", Color.GREEN);
            ParametersUtil.setSecrets( username, password, response.getToken());
            channel.shutdown();
            NavHostFragment.findNavController(this).navigate(R.id.action_from_login_to_settings);
        } catch (StatusRuntimeException e) {
            setLabel(e.getStatus().getDescription(), Color.RED);
        } finally {
            binding.loadingIndicator.setVisibility(View.GONE);
        }
    }

    private UserService.JwtResponse authorize(String username, String password) {
        var jwtRequest = UserService.JwtRequest.newBuilder()
                .setLogin(username)
                .setPass(password)
                .build();
        return authClient.authenticate(jwtRequest);
    }

    private void setLabel(String message, int color) {
        binding.messageLabel.setText(message);
        binding.messageLabel.setTextColor(color);
    }

    private void loadCredentials() {
        try {
            String username = ParametersUtil.getUsername();
            String password = ParametersUtil.getPassword();
            if (username != null) {
                binding.usernameEntry.setText(username);
            }

            if (password != null) {
                binding.passwordEntry.setText(password);
            }
        } catch (Exception ignored) {

        }
    }

}



