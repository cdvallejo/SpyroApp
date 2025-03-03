package dam.pmdm.spyrothedragon;
import static java.security.AccessController.getContext;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GuideBinding guideBinding;
    private NavController navController = null;
    private int tutorialStep = 0;
    private View[] tutorialScreens;
    private Boolean needToStartGuide = true; // Controla si la guía debe mostrarse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar);

        // Inflar el layout principal y el guide.xml
        guideBinding = GuideBinding.inflate(getLayoutInflater());
        binding.tutorialContainer.addView(guideBinding.getRoot());

        // Cargar SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        needToStartGuide = sharedPreferences.getBoolean("tutorial_completed", true);

        // Configurar botones del tutorial y sus sonidos
        guideBinding.btnStartTutorial.setOnClickListener(v -> {
            // Reproducir el sonido antes de iniciar el tutorial
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.empezar_sfx);
            mediaPlayer.start();

            // Liberar el MediaPlayer cuando termine de reproducirse
            mediaPlayer.setOnCompletionListener(mp -> mp.release());

            // Iniciar el tutorial
            updateTutorialStep();
        });
        // Binding de botones
        guideBinding.btnEndTutorial.setOnClickListener(v -> endTutorial());
        guideBinding.btnContinue.setOnClickListener(v -> updateTutorialStep());
        guideBinding.btnFinTutorial.setOnClickListener(v -> {
            // Usar MediaPlayer
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.finalizar_sfx);
            mediaPlayer.start();

            // Establecer el listener para cuando la reproducción termine
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Liberar el MediaPlayer cuando la reproducción termine
                    mp.release();
                }
            });
            enableNavigation();
            endTutorial();
        });

        // Configurar navegación
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);

        }
        // Desactivamos la flecha de volver atrás
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters || destination.getId() == R.id.navigation_worlds || destination.getId() == R.id.navigation_collectibles) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);
        // Inflamos todoas las páginas que tiene el array tutorialScreens
        tutorialScreens = new View[]{
                guideBinding.tutorialScreen1,
                guideBinding.tutorialScreen2,
                guideBinding.tutorialScreen3,
                guideBinding.tutorialScreen4,
                guideBinding.tutorialScreen5,
                guideBinding.tutorialScreen6,
        };

        // Verifica si debe iniciarse la guía
        if (needToStartGuide) {
            disableNavigation();
            startTutorial();
        }
    }
    // Identifica las pestañas del Bottom Menu
    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }

    /**
     * Deshabilita la navegación si el tutorial aún no ha sido completado.
     * Lo hago mediante un for porque directamente desactivando en conjunto no funciona
     */
    private void disableNavigation() {

        // Deshabilitar elementos del BottomNavigation
        for (int i = 0; i < binding.navView.getMenu().size(); i++) {
            binding.navView.getMenu().getItem(i).setEnabled(false);
        }
        // Verifica si el menú ya está inflado antes de acceder a él y deshabilitarlo
        if (binding.toolbar.getMenu().findItem(R.id.action_info) != null) {
            binding.toolbar.getMenu().findItem(R.id.action_info).setEnabled(false);
        }
    }

    /**
     * Habilita la navegación una vez que el tutorial ha sido completado.
     */
    private void enableNavigation() {

        // Habilitar elementos del BottomNavigation
        for (int i = 0; i < binding.navView.getMenu().size(); i++) {
            binding.navView.getMenu().getItem(i).setEnabled(true);
        }
    }

    /**
     * Inicia el tutorial y mantiene la navegación deshabilitada.
     */
    private void startTutorial() {
        binding.tutorialContainer.setVisibility(View.VISIBLE);
        tutorialStep = 0;
        tutorialScreens[tutorialStep].setVisibility(View.VISIBLE);
    }

    /**
     * Termina el tutorial, habilita la navegación y guarda que ya no es necesario mostrarlo.
     */
    private void endTutorial() {
        binding.tutorialContainer.setVisibility(View.GONE);
        needToStartGuide = false;
        enableNavigation();

        // Guardar en SharedPreferences que el tutorial ha sido completado
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("tutorial_completed", false);
        editor.apply();
    }

    /**
     * Actualiza el paso del tutorial y muestra la siguiente pantalla.
     */
    private void updateTutorialStep() {
        guideBinding.btnEndTutorial.setVisibility(View.VISIBLE);
        guideBinding.showBubble.setVisibility(View.VISIBLE);
        guideBinding.btnContinue.setVisibility(View.VISIBLE);

        // Establecemos la animación de la burbuja
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                guideBinding.showBubble, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                guideBinding.showBubble, "scaleY", 1f, 0.5f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(
                guideBinding.btnContinue, "alpha", 0f, 1f);

        scaleX.setRepeatCount(3);
        scaleY.setRepeatCount(3);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY).before(fadeIn);
        animatorSet.setDuration(1000);
        animatorSet.start();

        // Mueve la burbuja dependiendo del paso del tutorial
        moveBubbleToStep(tutorialStep);

        if (tutorialStep < tutorialScreens.length) {
            // Cargar la transición desde el XML
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_transition);
            // Aplicar la transición al contenedor del tutorial
            TransitionManager.beginDelayedTransition(binding.tutorialContainer, transition);
            // Ocultamos el tutorial del paso anterior
            tutorialScreens[tutorialStep].setVisibility(View.GONE);

            tutorialStep++;

            // Si ha llegado a "Mundos" cambiamos tab
            if (tutorialStep == 2) {
                navController.navigate(R.id.navigation_worlds);
            }

            // Si ha llegado a "Coleccionables" cambiamos tab
            if (tutorialStep == 3) {
                navController.navigate(R.id.navigation_collectibles);
            }

            // Si ha pasado al final volvemos al tab inicial
            if (tutorialStep == 5) {
                navController.navigate(R.id.navigation_characters);
                //Desactivamos el botón saltar guía, el showBubble y el Continuar
                guideBinding.btnEndTutorial.setVisibility(View.GONE);
                guideBinding.showBubble.setVisibility(View.GONE);
                guideBinding.btnContinue.setVisibility(View.GONE);
            }
            // Activamos el fragmento del layout correspondiente
            if (tutorialStep < tutorialScreens.length) {
                tutorialScreens[tutorialStep].setVisibility(View.VISIBLE);
            }

            // Aplicar la animación de fade_in del XML
            Animation fadeInBubble = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            tutorialScreens[tutorialStep].startAnimation(fadeInBubble);

            // Usar MediaPlayer para el sonido de cambio de animación del bubble
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.continuar_sfx);
            mediaPlayer.start();

            // Establecer el listener para cuando la reproducción termine
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Liberar el MediaPlayer cuando la reproducción termine
                    mp.release();
                }
            });
        }

    }

    private void moveBubbleToStep(int step) {
        int newMarginStart = 0;
        int newMarginTop = 0;

        switch (step) {
            case 0:
                newMarginStart = 25;
                newMarginTop = 900;
                break;
            case 1:
                newMarginStart = 425;
                newMarginTop = 900;
                break;
            case 2:
                newMarginStart = 825;
                newMarginTop = 900;
                break;
            case 3:
                newMarginStart = 940;
                newMarginTop = -80;
                break;
        }

        // Obtener el layout params del botón y actualizar la posición
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideBinding.showBubble.getLayoutParams();
        params.setMargins(newMarginStart, newMarginTop, 0, 0);
        guideBinding.showBubble.setLayoutParams(params);

        // Animar el movimiento con ObjectAnimator
        ObjectAnimator moveX = ObjectAnimator.ofFloat(guideBinding.showBubble, "translationX", guideBinding.showBubble.getTranslationX(), newMarginStart);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(guideBinding.showBubble, "translationY", guideBinding.showBubble.getTranslationY(), newMarginTop);

        AnimatorSet moveSet = new AnimatorSet();
        moveSet.playTogether(moveX, moveY);
        moveSet.setDuration(500); // Ajusta la duración de la animación
        moveSet.start();
    }

}
