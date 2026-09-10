/**
 * Class which represents a factory of renderers, which  constructs
 * different types of renderers according to the renderer needed to construct.
 */
public class RendererFactory {
    /**
     * Method that takes a renderer name and calls the appropriate constructor
     * according to the renderer type needed.
     * @param rendererName the renderer name needed.
     * @return object of the appropriate type of renderer, else, if the name is not
     * legal, it returns null.
     */
    public Renderer buildRenderer(String rendererName){
        switch (rendererName){
            case "console":
                return new ConsoleRenderer();
            case "none":
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
