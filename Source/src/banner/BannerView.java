package banner;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// Customer would like a banner with logo and name so we created this class to make it easy to add anywhere
public class BannerView extends HBox {

    private ImageView iconView;
    private Text label;

    public BannerView() {
        // set up properties

        // transparent so it can go on any background
        super.setStyle("-fx-background-color: transparent;");
        super.setAlignment(Pos.CENTER_LEFT);
        super.setSpacing(28);

        // create the icon image
        this.iconView = new ImageView();
        Image image = new Image("banner/BirdLogo-Outlined.png");
        iconView.setImage(image);
        iconView.setFitWidth(50);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.setCache(true);

        // create the title label
        this.label = new Text("BIRDSTUFZ");
        label.setFont(Font.font("Roboto", FontWeight.BLACK, 24));
        label.setFill(Color.WHITE);
        //super.setMargin(title, new Insets(20, 0, 0, 20));

        // add subviews
        super.getChildren().add(iconView);
        super.getChildren().add(label);

    }

}
