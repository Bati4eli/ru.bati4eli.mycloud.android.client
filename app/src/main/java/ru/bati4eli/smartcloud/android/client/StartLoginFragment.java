package ru.bati4eli.smartcloud.android.client;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import io.grpc.StatusRuntimeException;
import lombok.SneakyThrows;
import ru.bati4eli.smartcloud.android.client.databinding.FragmentLoginBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;
import ru.bati4eli.smartcloud.android.client.utils.TokenValidator;

public class StartLoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ParametersUtil.setContext(requireContext());
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.loginButton.setOnClickListener(v -> onLoginButtonClicked());
        loadCredentials();

        if (TokenValidator.isTokenExpired(ParametersUtil.getToken())) {
            onLoginButtonClicked();
        } else {
            navigateNextPage();
        }
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
            grpcService.authorize(username, password);
            setLabel("Успешная авторизация!", Color.GREEN);
//            grpcService.shutdown();
            navigateNextPage();
        } catch (StatusRuntimeException e) {
            setLabel(e.getStatus().getDescription(), Color.RED);
        } catch (Exception e) {
            setLabel(e.getMessage(), Color.RED);
        } finally {
            binding.loadingIndicator.setVisibility(View.GONE);
        }
    }

    private void navigateNextPage() {
        if (ParametersUtil.getNeedSetupPage()) {
            NavHostFragment.findNavController(this).navigate(R.id.action_from_login_to_settings);
        } else {
            // Navigation to MainActivity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
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



