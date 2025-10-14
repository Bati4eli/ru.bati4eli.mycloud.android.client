package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.CardFaceBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FaceViewHolder extends AbstractViewHolder<FaceCardModel> {

    private CardFaceBinding binding;

    public FaceViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.card_face);
        binding = CardFaceBinding.bind(super.itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bind(FaceCardModel face, OnItemClickListener<FaceCardModel> listener) {
        try {
            binding.labelText.setText(face.getFaceName());
            binding.amountText.setText(face.getAmount().toString());
            setupIcon(ShortInfo.of(face), binding.faceImage, DownloadType.FACE);
            setupOnClickListener(binding.getRoot(), face, listener);
        } catch (Throwable e) {
            Log.e(TAG, "FaceViewHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }


}
