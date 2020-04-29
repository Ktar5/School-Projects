
//mvc

public class MVCTester {
    public static Controller controller;
    //Controller = Buttons and action listeners
    //Model = the model class
    //View = Text Area / Frame / Etc.

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        controller = new Controller(view, model);
    }

}
