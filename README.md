# SpyroApp - Guía interactiva

Este proyecto de Android es una tarea en la que, a partir de una app dada sobre el videojuego "Spyro The Dragon", había que realizar un tutorial interactivo como bienvenida a la app. Incluye un "Easter Egg": un vídeo oculto al pulsar
cuatro veces sobre la imagen de la Gema en la pestaba de "Coleccionables".
Desarrollado para el módulo de Programación Multimedia y Dispositivos Móviles (FP DAM - IES Agudaulce).

## Características principales:

- <b>Visualización</b> de tres listas (personajes, mundos y coleccionables) sobre el videojuego "Spyro The Dragon".
- <b>Tutorial interactivo</b> que se abre al iniciar la app por primera vez.
- <b>Easter Egg</b> con un vídeo oculto que se activa al clicar 4 veces sobre la imagen del coleccionable "Gemas"-
- <b>Tecnologías utilizadas:</b>
  * Framework: Android SDK
  * Lenguaje: Java
  * Componentes:
     * RecyclerView 
     * Fragment
     * Navigation Component
     * View Bindding
     * Coordinator Layout
     * Fondos y contenidos con Drawable
     * ObjectAnimator
     * VideoPlayer
     * MediaPlayer
     * Transitions
  
## Instrucciones de uso:
Para descargar el repositorio del proyecto e instalarlo:
 * <b>Opción 1</b>: Pulsar sobre el botón verde CODE y obtener la url Https del proyecto. Desde Android Studio, instalar desde "New Project Version Control".
 * <b>Opción 2</b>: También se puede descargar el zip del proyecto, exportar y abrir localmente.
 * Para poder ejecutar la app hay que tener en cuanta que ha sido compilada con el siguiente Sdk:
   * compileSdk = 35
   * minSdk = 31
   * targetSdk = 34

(App probada con Pixel 7a API 34 y Medium Phone API 35)

## Conclusiones del desarrollador:
En esta tarea, bastante más liviana que las anteriores, he aprendido cómo programar efectos multimedia en una aplicación Android. Es un mundo muy amplio donde se pueden hacer virguerías y efectos complejos, reconozco que he recurrido a efectos más simples y sencillos. Es imprescindible que estos vayan ligados a una experiencia de usuario satisfactoria y clara. Por ejemplo, soy consciente de que la burbuja que señala cada apartado debería ser más transparente o dejar su interior vacío para que se vea claramente el nombre de cada pestaña (poco después de realizar la entrega caí en que existe la forma de anillo). Lo más difícil ha sido, como siempre, el comienzo: idear cómo empezar y qué pasos seguir. El dilema ha sido si hacer cada paso del tutorial en distintos layouts xml o en uno solo. Un xml por paso me parecía poco optimizado, y como un único layout no lograba imaginar su implementación. Una IA me sugirió almacenar cada paso como un array de views de FrameLayout que se van mostrando y desactivando. Me pregunto de qué modo, siguiendo este sistemna de único layout, podría haberlo optimizado mejor, ya que la función que lo va mostrando tiene muchos condicionantes que, seguramente, podrían haberse simplificado con un código mucho mejor.

### He aprendido durante el proceso:
A confeccionar un tutorial sobre el layout de una aplicación, a hacer animaciones con elementos drawables, a insertar vídeo y sonidos, a bloquear la navegación, a customizar elementos de diseño y a realizar transiciones entre layouts.

## Capturas de pantalla:
![spyro1](https://github.com/user-attachments/assets/17a26a2c-2400-4f6b-9fbf-98da0df00a7a) ![spyro2](https://github.com/user-attachments/assets/cf047cfb-0b50-4a1c-8514-774a3a1370dd) 
![spyro3](https://github.com/user-attachments/assets/d88ca329-5cac-4f16-8856-b95b5727a580)



