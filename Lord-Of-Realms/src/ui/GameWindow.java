

/*
  -> represent the main game window.
  -> responsible only for displaying the user interface.
  -> forwarding user actions to the game logic.
*/








package ui;
import combat.*;
import inventory.*;
import player.Player;
import story.Choice;
import story.ChoiceType;
import story.StoryManager;
import story.StoryScene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    private static final String GAME_TITTLE = "LORD OF REALMS";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    private JPanel mainPanel;

    //Status  panel...
    private JPanel topPanel;
    private JPanel inventoryPanel;
    private JTextArea inventoryTextArea;
    private JScrollPane inventoryScrollPane;
    private JPanel statusPanel;
    private JLabel nameLabel;
    private JLabel realmLabel;
    private JLabel hpLabel;
    private JLabel qiLabel;
    private JLabel expLabel;
    private JLabel defenseLabel;


    //story panel...
    private JPanel storyPanel;
    private JTextArea storyTextArea;
    private JScrollPane storyScrollPane;


    //Choice Panel...
    private JPanel choicePanel;
    private JButton choice1Button;
    private JButton choice2Button;
    private JButton choice3Button;
    private JButton choice4Button;
    private JButton combatBackButton;
    private JButton combatContinueButton;
    private JButton skillsButton;




    private Player player;
    private StoryManager storyManager;
    private CombatManager combatManager;
    private Choice combatChoice;
    private boolean inCombat;
    private boolean selectingCombatItem;
    private boolean selectingCombatAttack;
    private List<Item> combatItems;
    private List<Item> combatWeapons;
    private boolean combatResult;
    private boolean selectingSkillScroll;
    private List<Item> skillScrolls = new ArrayList<>();





    public GameWindow(Player player, StoryManager storyManager){
        this.player = player;
        this.storyManager = storyManager;
        this.storyManager.startStory(1);

        initialize();
        updatePlayerInfo();
        updateInventory();
        updateScene();// Actual window Frame
    }

    // Details of window
    public void initialize(){
        configureWindow();
        createComponents();
        layoutComponents();
        registerListeners();
    }


    private void registerListeners() {

        choice1Button.addActionListener( e-> handleButtonClick(0));

        choice2Button.addActionListener(e -> handleButtonClick(1));

        choice3Button.addActionListener( e-> handleButtonClick(2));

        choice4Button.addActionListener( e-> handleButtonClick(3));

        combatBackButton.addActionListener(e -> showMainCombatActions());

        combatContinueButton.addActionListener(e -> handleCombatContinue());

        skillsButton.addActionListener(e -> showSkillScrolls());

    }


    // it gives player in combat or story
    private void handleButtonClick(int buttonIndex) {

        if (inCombat) {
            handleCombatAction(buttonIndex);

        } else if(selectingSkillScroll){

            handleSkillScroll(buttonIndex);
        }
        else {
            handleChoice(buttonIndex);
        }
    }

    private void handleSkillScroll(int buttonIndex){

        if(buttonIndex == skillScrolls.size()){

            selectingSkillScroll = false;

            updateScene();

            return;
        }

        if(buttonIndex >= skillScrolls.size()){
            return;
        }

        Item scroll = skillScrolls.get(buttonIndex);

        Skill skill = scroll.getSkill();

        if(skill == null){
            return;
        }


        boolean learned = player.learnSkillFromScroll(scroll);

        if(learned){

            setStoryText(
                    "you learned " + skill.getName()
            );

            updatePlayerInfo();
        }
        else{
            setStoryText(
                    "You cannot learn " + skill.getName() + "."
            );

            selectingSkillScroll = false;;

            updateScene();
        }
    }

    private void handleCombatContinue(){

        combatContinueButton.setVisible(false);
        combatResult = false;

        if(combatManager.getEnemy().isAlive()){

            storyManager.startStory(1);
        } else{

            int nextSceneId = combatChoice.getNextSceneId();
            storyManager.completeCombat(nextSceneId);
        }

        updateScene();
        updatePlayerInfo();
    }


    // buttons for combat
    private void handleCombatAction(int actionIndex){

        if(selectingCombatItem){

            useCombatItem(actionIndex);
            return;
        }

        if(selectingCombatAttack){

           useCombatWeapon(actionIndex);
            return;
        }
        if(actionIndex == 0) {

            selectingCombatAttack = true;

            showCombatWeapons();
            return;

        }

        else if(actionIndex == 1){

            selectingCombatItem = true;

            showCombatItems();

            return;


        }

        if(actionIndex == 2){
            setStoryText("skill are not implement yet");
            return;
        }

        if (actionIndex == 3) {

            setStoryText("You try to escape.");

            return;
        }


    }



    private void showCombatResult(){
        inCombat = false;

        choice1Button.setVisible(false);
        choice2Button.setVisible(false);
        choice3Button.setVisible(false);
        choice4Button.setVisible(false);

        combatBackButton.setVisible(false);

        if (combatManager.getEnemy().isAlive()) {

            setStoryText(
                    combatManager.getEnemy().getName()
                            + " defeated you."
            );

        } else {

            setStoryText(
                    "You defeated "
                            + combatManager.getEnemy().getName()
                            + "!\n\n"
                            + "Exp gained: "
                            + combatManager.getEnemy().getExpReward()
            );
        }

        combatContinueButton.setVisible(true);

        updatePlayerInfo();
    }




    public void showCombatItems(){

        combatItems.clear();

        List<InventoryItem> items = player.getInventory().getItems();

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        int buttonIndex = 0;

        for(InventoryItem inventoryItem : items){

            Item item = inventoryItem.getItem();

            if(item.isConsumable()){

                if(buttonIndex >= buttons.length){
                    break;
                }

                combatItems.add(item);

                buttons[buttonIndex].setText(
                        item.getName() + " X" + inventoryItem.getQuantity()
                );
                buttons[buttonIndex].setVisible(true);

                buttonIndex++;
            }
        }

        while(buttonIndex < buttons.length){
            buttons[buttonIndex].setVisible(false);
            buttonIndex++;
        }

        combatBackButton.setVisible(true);
    }

    private void showCombatWeapons() {

        combatWeapons.clear();

        List<InventoryItem> items =
                player.getInventory().getItems();

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        int buttonIndex = 0;

        buttons[buttonIndex].setText("Fist");
        buttons[buttonIndex].setVisible(true);

        combatWeapons.add(null);

        buttonIndex++;


        // adding weapon from item:
        for (InventoryItem inventoryItem : items) {

            Item item = inventoryItem.getItem();

            if (item.getType() == ItemType.WEAPON) {

                if (buttonIndex >= buttons.length) {
                    break;
                }

                combatWeapons.add(item);

                buttons[buttonIndex].setText(item.getName());
                buttons[buttonIndex].setVisible(true);

                buttonIndex++;
            }
        }

        while (buttonIndex < buttons.length) {
            buttons[buttonIndex].setVisible(false);
            buttonIndex++;
        }

        combatBackButton.setVisible(true);
    }

    private void useCombatWeapon(int weaponIndex){

        if(weaponIndex >= combatWeapons.size()){
            return;
        }

        Item weapon  = combatWeapons.get(weaponIndex);

        String result;
        if(weapon == null){
            result = combatManager.playerAttack();
        }else{
            result = combatManager.playerAttack(weapon);
        }

        setStoryText(result);

        updatePlayerInfo();

        if(combatManager.isCombatOver()){
            showCombatResult();

            return;
        }

        selectingCombatAttack = false;
        setCombatActions(combatManager.getActions());

    }



    private void useCombatItem(int itemIndex){

        if(itemIndex >= combatItems.size()){
            return;
        }

        Item item = combatItems.get(itemIndex);

        String result = combatManager.playerUseItem(item);

        if(result == null){
            return;
        }

        setStoryText(result);

        updatePlayerInfo();
        updateInventory();

        if(combatManager.isCombatOver()){

            inCombat = false;
            selectingCombatItem = false;

            int nextSceneId = combatChoice.getNextSceneId();

            storyManager.completeCombat(nextSceneId);

            updateScene();
            updatePlayerInfo();

            return;
        }

        selectingCombatItem = false;

        setCombatActions(combatManager.getActions());


    }


    private void showMainCombatActions(){

        selectingCombatItem = false;
        selectingCombatAttack =  false;

        combatBackButton.setVisible(false);

        setCombatActions(combatManager.getActions());
    }



    private void handleChoice(int choiceIndex){

        StoryScene scene = storyManager.getCurrentScene();
        Choice choice = scene.getChoices().get(choiceIndex);

        if(choice.getType() == ChoiceType.COMBAT){
            enterCombat(choice);
            return;
        }

       boolean success =  storyManager.selectChoice(choiceIndex);
        if(success) {
            updateScene();
            updateInventory();
            updatePlayerInfo();
        }else{
            setStoryText(storyManager.getLastMessage());
        }
    }
    // temp
    private void enterCombat(Choice choice) {

        inCombat = true;

        combatChoice = choice;

        combatManager = new CombatManager(player,choice.getEnemy());

        combatManager.startCombat();

        showCombatStart();

        setCombatActions(combatManager.getActions());

    }

    private void showCombatStart() {

        Enemy enemy = combatManager.getEnemy();

        setStoryText(
                enemy.getName() + " appears!\n\n" +
                        enemy.getName() + " HP: " +
                        enemy.getHp() + "/" + enemy.getMaxHp() + "\n\n" +
                        player.getName() + " HP: " +
                        player.getHealthText()
        );
    }


    private void showSkillScrolls(){

        selectingSkillScroll = true;

        skillScrolls.clear();

        for(InventoryItem inventoryItem : player.getInventory().getItems()){

            Item item = inventoryItem.getItem();

            if(item.getType() == ItemType.SKILL){
                skillScrolls.add(item);
            }
        }

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        int buttonIndex = 0;

        for(Item scroll : skillScrolls){

            if(buttonIndex >= buttons.length -1){
                break;
            }

            buttons[buttonIndex].setText(scroll.getName());
            buttons[buttonIndex].setVisible(true);

            buttonIndex++;
        }

        buttons[buttonIndex].setText("Back");
        buttons[buttonIndex].setVisible(true);

        buttonIndex++;

        // Hide remaining buttons
        while (buttonIndex < buttons.length) {

            buttons[buttonIndex].setVisible(false);
            buttonIndex++;
        }
    }


    // layouts,fonts,colors,borders, sizes etc.......
    private void layoutComponents() {
        mainPanel.setLayout(new BorderLayout());
      //  mainPanel.add(statusPanel, BorderLayout.NORTH);


        topPanel.setLayout(new BorderLayout());
        topPanel.add(statusPanel, BorderLayout.WEST);
        topPanel.add(inventoryPanel,BorderLayout.CENTER);

        mainPanel.add(topPanel,BorderLayout.NORTH);
        mainPanel.add(storyPanel, BorderLayout.CENTER);
        mainPanel.add(choicePanel, BorderLayout.SOUTH);
        add(mainPanel);




        statusPanel.setBackground(Color.RED);
        storyPanel.setBackground(Color.BLUE);
        choicePanel.setBackground(Color.GREEN);

        // status panel...

        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.add(inventoryScrollPane, BorderLayout.CENTER);

        statusPanel.setLayout(
                new BoxLayout(statusPanel,BoxLayout.Y_AXIS)
        );
        nameLabel.setFont(new Font("font",Font.PLAIN,16));
        statusPanel.add(nameLabel);
        realmLabel.setFont(new Font("font",Font.PLAIN,16));
        statusPanel.add(realmLabel);
        hpLabel.setFont(new Font("font",Font.PLAIN,16));
        statusPanel.add(hpLabel);
        qiLabel.setFont(new Font("font",Font.PLAIN,16));
        statusPanel.add(qiLabel);
        expLabel.setFont(new Font("font",Font.PLAIN,16));
        statusPanel.add(expLabel);
        defenseLabel.setFont(new Font("font",Font.PLAIN,16));
        statusPanel.add(defenseLabel);

       // inventoryLabel.setFont(new Font("font",Font.PLAIN,16));
       // statusPanel.add(inventoryLabel);

        // story panel...
        storyPanel.setLayout(new BorderLayout());
        storyPanel.add(storyScrollPane,BorderLayout.CENTER);

        // choice panel....
        choicePanel.add(choice1Button);
        choicePanel.add(choice2Button);
        choicePanel.add(choice3Button);
        choicePanel.add(choice4Button);
        choicePanel.add(combatBackButton);
        choicePanel.add(combatContinueButton);
        choicePanel.add(skillsButton);



    }


    // all my panel, buttons, labels, text area etc......
    private void createComponents() {
        mainPanel = new JPanel();


        //status panel...
        statusPanel = new JPanel();
        nameLabel = new JLabel();
        realmLabel = new JLabel();
        hpLabel = new JLabel();
        qiLabel = new JLabel();
        expLabel = new JLabel();
        defenseLabel = new JLabel();

       // inventoryLabel = new JLabel("Inventory : Empty");

        topPanel = new JPanel();
        inventoryPanel = new JPanel();

        inventoryTextArea = new JTextArea();
        inventoryTextArea.setEditable(false);
        inventoryTextArea.setLineWrap(true);
        inventoryTextArea.setWrapStyleWord(true);

        inventoryScrollPane = new JScrollPane(inventoryTextArea);

        //story panel....
        storyPanel = new JPanel();
        storyTextArea = new JTextArea();
        storyTextArea.setFont(new Font("GC Omega", Font.BOLD,20));


        storyScrollPane = new JScrollPane(storyTextArea);
        storyTextArea.setEditable(false);
        storyTextArea.setLineWrap(true);
        storyTextArea.setWrapStyleWord(true);

        //choice panel...
        choicePanel = new JPanel();
        choice1Button = new JButton();
        choice2Button = new JButton();
        choice3Button = new JButton();
        choice4Button = new JButton();
        combatBackButton = new JButton("Back");
        combatBackButton.setVisible(false);
        combatContinueButton = new JButton("Continue");
        combatContinueButton.setVisible(false);
        skillsButton = new JButton("Skills");


        combatItems = new ArrayList<>();
        combatWeapons = new ArrayList<>();





    }


    private void configureWindow() {
        setTitle(GAME_TITTLE);
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public void setStoryText(String text){
       
        storyTextArea.setText(text);
    }

    public void setChoices(List<Choice> choices){
        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button

        };

        for(int i = 0; i < buttons.length; i++){
            if(i < choices.size()){
                buttons[i].setText(choices.get(i).getText());
                buttons[i].setVisible(true);
            }else{
                buttons[i].setVisible(false);
            }
        }
    }

    private void setCombatActions(List<CombatAction> actions) {

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        for (int i = 0; i < buttons.length; i++) {

            if (i < actions.size()) {
                buttons[i].setText(actions.get(i).getName());
                buttons[i].setVisible(true);
            } else {
                buttons[i].setVisible(false);
            }
        }
    }



    private void updateScene() {

        StoryScene scene = storyManager.getCurrentScene();



        setStoryText(scene.getStoryText());
        setChoices(scene.getChoices());

    }

    public void updateHealth(int hp){
        hpLabel.setText("HP : " + hp + "/100");
    }

    private void updatePlayerInfo(){
        nameLabel.setText(("Name : " + player.getName()));
        realmLabel.setText("Realm : "+ player.getRealmName());
        hpLabel.setText("HP : "+ player.getHealthText());
        qiLabel.setText("Qi : " + player.getQiText());
        expLabel.setText("EXp : " + player.getExp());
        defenseLabel.setText("Defense : " + player.getDefense());
    }

    private void updateInventory(){

        inventoryTextArea.setText(
                "Inventory: \n" +
                player.getInventory().getItemsText()
        );
    }


    public void showWindow(){
        setVisible(true);
    }

}
