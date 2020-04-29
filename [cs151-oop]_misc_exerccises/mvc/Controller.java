

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void addNewLine(String line){
        model.addNewLine(line);
        view.update(model);
    }


}
