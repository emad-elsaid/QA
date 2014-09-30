/*

Arabic Stemmer: This program stems Arabic words and returns their root.
Copyright (C) 2002 Shereen Khoja

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

Computing Department
Lancaster University
Lancaster
LA1 4YR
s.Khoja@lancaster.ac.uk

*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ArabicStemmerGUI extends JFrame
{
    //--------------------------------------------------------------------------

    // the arabic stemmer
    protected ArabicStemmer arabicStemmer;

    // the components
    protected Components components;

    // the default window title
    protected String defaultWindowTitle = new String ( " Arabic Stemmer " );

    // the statistics window
    protected StatisticsWindow statisticsWindow;

    // the roots window
    protected RootsWindow rootsWindow;

    //--------------------------------------------------------------------------

    protected class InputFileButtons extends JPanel
    {
        // the buttons
        JButton openButton;
        JButton closeButton;
        JButton saveButton;
        JButton saveAsButton;

        // constructor
        InputFileButtons ( )
        {
            // the layout manager
            GridLayout gridLayout = new GridLayout ( );
            setLayout ( gridLayout );

            // the buttons
            openButton = new JButton ( "Open..." );
            add ( openButton );
            closeButton = new JButton ( "Close" );
            add ( closeButton );
            saveButton = new JButton ( "Save" );
            add ( saveButton );
            saveAsButton = new JButton ( "Save As..." );
            add ( saveAsButton );
        }
    }

    //--------------------------------------------------------------------------

    protected class OutputFileButtons extends JPanel
    {
        // the buttons
        JButton stemButton;
        JButton statisticsButton;
        JButton rootsButton;
        JButton saveAsButton;

        // constructor
        OutputFileButtons ( )
        {
            // the layout manager
            GridLayout gridLayout = new GridLayout ( );
            setLayout ( gridLayout );

            // the buttons
            stemButton = new JButton ( "Stem" );
            add ( stemButton );
            statisticsButton = new JButton ( "Statistics..." );
            add ( statisticsButton );
            rootsButton = new JButton ( "Roots..." );
            add ( rootsButton );
            saveAsButton = new JButton ( "Save As..." );
            add ( saveAsButton );
        }
    }

    //--------------------------------------------------------------------------

    protected class RemainingButtons extends JPanel
    {
        // the buttons
        JButton aboutButton;
        JButton exitButton;

        // constructor
        RemainingButtons ( )
        {
            // the layout manager
            GridLayout gridLayout = new GridLayout ( );
            setLayout ( gridLayout );

            // the buttons
            aboutButton = new JButton ( "About..." );
            add ( aboutButton );
            JButton dummyButtonA = new JButton ( );
            add ( dummyButtonA );
            JButton dummyButtonB = new JButton ( );
            add ( dummyButtonB );
            exitButton = new JButton ( "Exit" );
            add ( exitButton );

            // hide the dummy buttons
            dummyButtonA.setVisible ( false );
            dummyButtonB.setVisible ( false );
        }
    }

    //--------------------------------------------------------------------------

    protected class InputFilePanel extends JPanel
    {
        // the text area
        JTextArea textArea;

        // the buttons
        InputFileButtons inputFileButtons;

        // constructor
        InputFilePanel ( )
        {
            // the layout manager
            final int horizontalGap = 0;
            final int verticalGap = 11;
            BorderLayout borderLayout = new BorderLayout ( horizontalGap, verticalGap );
            setLayout ( borderLayout );

            // the border
            final String borderTitle = " Input File ";
            final int left = 8;
            final int right = left;
            final int top = 0;
            final int bottom = left;
            setBorder ( BorderFactory.createCompoundBorder ( BorderFactory.createTitledBorder ( borderTitle ), BorderFactory.createEmptyBorder ( top, left, bottom, right ) ) );

            // the text area
            final int rows = 10;
            final int columns = 60;
            textArea = new JTextArea ( rows, columns );
            textArea.setLineWrap ( true );
            textArea.setWrapStyleWord ( true );
            textArea.setComponentOrientation ( ComponentOrientation.RIGHT_TO_LEFT );
            JScrollPane scrollPane = new JScrollPane ( textArea );
            scrollPane.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setHorizontalScrollBarPolicy ( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setBorder ( BorderFactory.createLoweredBevelBorder ( ) );
            add ( scrollPane, BorderLayout.CENTER );

            // the buttons
            inputFileButtons = new InputFileButtons ( );
            add ( inputFileButtons, BorderLayout.SOUTH );
        }
    }

    //--------------------------------------------------------------------------

    protected class StatusBarPanel extends JPanel
    {
        // the label
        JLabel label;

        // constructor
        StatusBarPanel ( )
        {
            // the layout manager
            GridLayout gridLayout = new GridLayout ( );
            setLayout ( gridLayout );

            // the border
            final int left = 2;
            final int right = left;
            final int top = 8;
            final int bottom = left;
            setBorder ( BorderFactory.createCompoundBorder ( BorderFactory.createEmptyBorder ( top, left, bottom, right ), BorderFactory.createCompoundBorder ( BorderFactory.createEtchedBorder ( ), BorderFactory.createEmptyBorder ( 2, 2, 2, 2 ) ) ) );

            // the label
            label = new JLabel ( "", JLabel.CENTER );
            add ( label );
        }
    }

    //--------------------------------------------------------------------------

    protected class OutputFilePanel extends JPanel
    {
        // the text area
        JTextArea textArea;

        // the buttons
        OutputFileButtons outputFileButtons;

        // constructor
        OutputFilePanel ( )
        {
            // the layout manager
            final int horizontalGap = 0;
            final int verticalGap = 11;
            BorderLayout borderLayout = new BorderLayout ( horizontalGap, verticalGap );
            setLayout ( borderLayout );

            // the border
            final String borderTitle = " Output File ";
            final int left = 8;
            final int right = left;
            final int top = 0;
            final int bottom = left;
            setBorder ( BorderFactory.createCompoundBorder ( BorderFactory.createTitledBorder ( borderTitle ), BorderFactory.createEmptyBorder ( top, left, bottom, right ) ) );

            // the text area
            final int rows = 10;
            final int columns = 60;
            textArea = new JTextArea ( rows, columns );
            textArea.setLineWrap ( true );
            textArea.setWrapStyleWord ( true );
            textArea.setComponentOrientation ( ComponentOrientation.RIGHT_TO_LEFT );
            JScrollPane scrollPane = new JScrollPane ( textArea );
            scrollPane.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setHorizontalScrollBarPolicy ( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setBorder ( BorderFactory.createLoweredBevelBorder ( ) );
            add ( scrollPane, BorderLayout.CENTER );

            // the buttons
            outputFileButtons = new OutputFileButtons ( );
            add ( outputFileButtons, BorderLayout.SOUTH );
        }
    }

    //--------------------------------------------------------------------------

    protected class ComboBoxesPanel extends JPanel
    {
        // the combo boxes
        JComboBox fontComboBox;
        JComboBox fontSizeComboBox;
        JComboBox encodingComboBox;
        JComboBox lookAndFeelComboBox;

        // constructor
        ComboBoxesPanel ( )
        {
            // the layout manager
            setLayout ( new BoxLayout ( this, BoxLayout.X_AXIS ) );

            // the border
            final int left = 2;
            final int right = left;
            final int top = 8;
            final int bottom = left;
            setBorder ( BorderFactory.createEmptyBorder ( top, left, bottom, right ) );

            // the font combo box
            JLabel fontLabel = new JLabel ( "Font" );
            fontComboBox = new JComboBox ( );
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment ( );
            Font [ ] fontArray = graphicsEnvironment.getAllFonts ( );
            for ( int index = 0; index < fontArray.length; index ++ )
            {
                fontComboBox.addItem ( makeSafeComboBoxString ( fontArray[ index ].getName ( ) ) );
                if ( fontComboBox.getItemAt( index ).toString( ).compareTo ( "Times New Roman" ) == 0 )
                {
                    fontComboBox.setSelectedIndex ( index );
                }
            }

            // the font size combo box
            fontSizeComboBox = new JComboBox ( );
            fontSizeComboBox.addItem ( makeSafeComboBoxString ( "12" ) );

            // the encoding combo box
            JLabel encodingLabel = new JLabel ( "Encoding" );
            encodingComboBox = new JComboBox ( );
            encodingComboBox.addItem ( makeSafeComboBoxString ( "UTF-8" ) );
            encodingComboBox.addItem ( makeSafeComboBoxString ( "UTF-16BE" ) );
            encodingComboBox.addItem ( makeSafeComboBoxString ( "UTF-16LE" ) );
            encodingComboBox.addItem ( makeSafeComboBoxString ( "UTF-16" ) );
            encodingComboBox.setSelectedIndex ( 3 );

            // the look and feel combo box
            JLabel lookAndFeelLabel = new JLabel ( "Look and Feel" );
            lookAndFeelComboBox = new JComboBox ( );
            lookAndFeelComboBox.addItem ( makeSafeComboBoxString ( "System" ) );
            lookAndFeelComboBox.addItem ( makeSafeComboBoxString ( "Motif" ) );
            lookAndFeelComboBox.addItem ( makeSafeComboBoxString ( "Java" ) );
            lookAndFeelComboBox.setSelectedIndex ( 0 );

            // add the components
            add ( Box.createHorizontalGlue ( ) );
            add ( Box.createHorizontalStrut ( 3 ) );
            add ( fontLabel );
            add ( Box.createHorizontalStrut ( 5 ) );
            add ( fontComboBox );
            add ( Box.createHorizontalStrut ( 2 ) );
            add ( fontSizeComboBox );
            add ( Box.createHorizontalStrut ( 10 ) );
            add ( encodingLabel );
            add ( Box.createHorizontalStrut ( 5 ) );
            add ( encodingComboBox );
            add ( Box.createHorizontalStrut ( 10 ) );
            add ( lookAndFeelLabel );
            add ( Box.createHorizontalStrut ( 5 ) );
            add ( lookAndFeelComboBox );
            add ( Box.createHorizontalStrut ( 3 ) );
            add ( Box.createHorizontalGlue ( ) );
        }
    }

    //--------------------------------------------------------------------------

    protected class RemainingButtonsPanel extends JPanel
    {
        // the buttons
        RemainingButtons remainingButtons;

        // constructor
        RemainingButtonsPanel ( )
        {
            // the layout manager
            setLayout ( new BoxLayout ( this, BoxLayout.X_AXIS ) );

            // the border
            final int left = 2;
            final int right = left;
            final int top = 8;
            final int bottom = left;
            setBorder ( BorderFactory.createEmptyBorder ( top, left, bottom, right ) );

            // the buttons
            remainingButtons = new RemainingButtons ( );
            add ( remainingButtons );
        }
    }

    //--------------------------------------------------------------------------

    protected class Components extends JPanel
    {
        // the panels
        InputFilePanel inputFilePanel;
        StatusBarPanel statusBarPanel;
        OutputFilePanel outputFilePanel;
        ComboBoxesPanel comboBoxesPanel;
        RemainingButtonsPanel remainingButtonsPanel;

        // constructor
        Components ( )
        {
            // the layout manager
            setLayout ( new BoxLayout ( this, BoxLayout.Y_AXIS ) );

            // the border
            final int left = 8;
            final int right = left;
            final int top = 4;
            final int bottom = left;
            setBorder ( BorderFactory.createEmptyBorder ( top, left, bottom, right ) );

            // the panels
            inputFilePanel = new InputFilePanel ( );
            add ( inputFilePanel );
            statusBarPanel = new StatusBarPanel ( );
            add ( statusBarPanel );
            outputFilePanel = new OutputFilePanel ( );
            add ( outputFilePanel );
            comboBoxesPanel = new ComboBoxesPanel ( );
            add ( comboBoxesPanel );
            remainingButtonsPanel = new RemainingButtonsPanel ( );
            add ( remainingButtonsPanel );
        }
    }

    //--------------------------------------------------------------------------

    protected class StatisticsWindowLabels extends JPanel
    {
        // the labels
        JLabel percentStemmedLabel;
        JLabel stemmingTimeLabel;
        JLabel stemmedWordsLabel;
        JLabel wordsNotStemmedLabel;
        JLabel stopwordsLabel;
        JLabel punctuationWordsLabel;
        JLabel nonLetterWordsLabel;
        JLabel totalWordsLabel;

        // constructor
        StatisticsWindowLabels ( )
        {
            // the layout manager
            GridLayout gridLayout = new GridLayout ( );
            setLayout ( gridLayout );

            // the left panel
            JPanel leftPanel = new JPanel ( );
            leftPanel.setLayout ( new BoxLayout ( leftPanel, BoxLayout.Y_AXIS ) );
            JLabel percentStemmedStaticLabel = new JLabel ( " Percent Stemmed " );
            JLabel stemmingTimeStaticLabel = new JLabel ( " Stemming Time " );
            percentStemmedLabel = new JLabel ( );
            stemmingTimeLabel = new JLabel ( );
            percentStemmedStaticLabel.setAlignmentX ( JLabel.CENTER_ALIGNMENT );
            stemmingTimeStaticLabel.setAlignmentX ( JLabel.CENTER_ALIGNMENT );
            percentStemmedLabel.setAlignmentX ( JLabel.CENTER_ALIGNMENT );
            stemmingTimeLabel.setAlignmentX ( JLabel.CENTER_ALIGNMENT );
            percentStemmedStaticLabel.setFont ( percentStemmedStaticLabel.getFont( ).deriveFont ( percentStemmedStaticLabel.getFont( ).getSize2D ( ) + 2 ) );
            percentStemmedStaticLabel.setFont ( percentStemmedStaticLabel.getFont( ).deriveFont ( Font.BOLD + Font.ITALIC ) );
            stemmingTimeStaticLabel.setFont ( stemmingTimeStaticLabel.getFont( ).deriveFont ( stemmingTimeStaticLabel.getFont( ).getSize2D ( ) + 2 ) );
            stemmingTimeStaticLabel.setFont ( stemmingTimeStaticLabel.getFont( ).deriveFont ( Font.BOLD + Font.ITALIC ) );
            percentStemmedLabel.setFont ( percentStemmedLabel.getFont( ).deriveFont ( percentStemmedLabel.getFont( ).getSize2D ( ) + 3 ) );
            percentStemmedLabel.setFont ( percentStemmedLabel.getFont( ).deriveFont ( Font.BOLD + Font.ITALIC ) );
            stemmingTimeLabel.setFont ( stemmingTimeLabel.getFont( ).deriveFont ( stemmingTimeLabel.getFont( ).getSize2D ( ) + 3 ) );
            stemmingTimeLabel.setFont ( stemmingTimeLabel.getFont( ).deriveFont ( Font.BOLD + Font.ITALIC ) );
            leftPanel.add ( Box.createVerticalGlue ( ) );
            leftPanel.add ( percentStemmedStaticLabel );
            leftPanel.add ( percentStemmedLabel );
            leftPanel.add ( Box.createVerticalStrut ( 5 ) );
            leftPanel.add ( stemmingTimeStaticLabel );
            leftPanel.add ( stemmingTimeLabel );
            leftPanel.add ( Box.createVerticalGlue ( ) );
            add ( leftPanel );

            // the right panel
            JPanel rightPanel = new JPanel ( );
            rightPanel.setLayout ( new GridLayout ( 6, 2 ) );
            JLabel totalWordsStaticLabel;
            rightPanel.add ( new JLabel ( " Stemmed Words " ) );
            rightPanel.add ( stemmedWordsLabel = new JLabel ( "", JLabel.RIGHT ) );
            rightPanel.add ( new JLabel ( " Words Not Stemmed " ) );
            rightPanel.add ( wordsNotStemmedLabel = new JLabel ( "", JLabel.RIGHT ) );
            rightPanel.add ( new JLabel ( " Stopwords " ) );
            rightPanel.add ( stopwordsLabel = new JLabel ( "", JLabel.RIGHT ) );
            rightPanel.add ( new JLabel ( " Punctuation Words " ) );
            rightPanel.add ( punctuationWordsLabel = new JLabel ( "", JLabel.RIGHT ) );
            rightPanel.add ( new JLabel ( " Non Letter Words " ) );
            rightPanel.add ( nonLetterWordsLabel = new JLabel ( "", JLabel.RIGHT ) );
            rightPanel.add ( totalWordsStaticLabel = new JLabel ( " Total Words " ) );
            rightPanel.add ( totalWordsLabel = new JLabel ( "", JLabel.RIGHT ) );
            totalWordsStaticLabel.setFont ( totalWordsLabel.getFont( ).deriveFont ( Font.BOLD ) );
            totalWordsLabel.setFont ( totalWordsLabel.getFont( ).deriveFont ( Font.BOLD ) );
            add ( rightPanel );
        }
    }

    //--------------------------------------------------------------------------

    protected class StatisticsWindowButtons extends JPanel
    {
        // the buttons
        JButton saveAsButton;
        JButton closeButton;

        // constructor
        StatisticsWindowButtons ( )
        {
            // the layout manager
            GridLayout gridLayout = new GridLayout ( );
            setLayout ( gridLayout );

            // the buttons
            saveAsButton = new JButton ( "Save As..." );
            add ( saveAsButton );
            closeButton = new JButton ( "Close" );
            add ( closeButton );
        }
    }

    //--------------------------------------------------------------------------

    protected class StatisticsWindow extends JFrame
    {
        // the labels
        StatisticsWindowLabels statisticsWindowLabels;

        // the tabbed pane
        JTabbedPane tabbedPane;

        // the text areas
        JTextArea stemmedWordsTextArea;
        JTextArea wordsNotStemmedTextArea;
        JTextArea stopwordsTextArea;

        // the buttons
        StatisticsWindowButtons statisticsWindowButtons;

        // constructor
        StatisticsWindow ( )
        {
            // change the window icon
            String pathToImage = new StringBuffer ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "Images" + System.getProperty ( "file.separator" ) + "WindowIcon.gif" ).toString ( );
            Toolkit toolkit = Toolkit.getDefaultToolkit ( );
            Image image = toolkit.getImage ( pathToImage );
            setIconImage ( image );

            // set the window title
            setTitle ( " Statistics " );

            // the panel
            JPanel panel = new JPanel ( );
            setContentPane( panel );

            // the layout manager
            final int horizontalGap = 0;
            final int verticalGap = 12;
            BorderLayout borderLayout = new BorderLayout ( horizontalGap, verticalGap );
            panel.setLayout ( borderLayout );

            // the border
            final int left = 11;
            final int right = left;
            final int top = left;
            final int bottom = left;
            panel.setBorder ( BorderFactory.createEmptyBorder ( top, left, bottom, right ) );

            // the labels
            statisticsWindowLabels = new StatisticsWindowLabels ( );
            panel.add ( statisticsWindowLabels, BorderLayout.NORTH );

            // the tabbed pane
            tabbedPane = new JTabbedPane ( );
            panel.add ( tabbedPane, BorderLayout.CENTER );

            // the stemmed words tab
            int rows = 10;
            int columns = 60;
            stemmedWordsTextArea = new JTextArea ( rows, columns );
            stemmedWordsTextArea.setLineWrap ( true );
            stemmedWordsTextArea.setWrapStyleWord ( true );
            stemmedWordsTextArea.setComponentOrientation ( ComponentOrientation.RIGHT_TO_LEFT );
            JScrollPane scrollPane = new JScrollPane ( stemmedWordsTextArea );
            scrollPane.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setHorizontalScrollBarPolicy ( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setBorder ( BorderFactory.createLoweredBevelBorder ( ) );
            tabbedPane.addTab ( " Stemmed Words ", scrollPane );

            // the words not stemmed tab
            rows = 10;
            columns = 60;
            wordsNotStemmedTextArea = new JTextArea ( rows, columns );
            wordsNotStemmedTextArea.setLineWrap ( true );
            wordsNotStemmedTextArea.setWrapStyleWord ( true );
            wordsNotStemmedTextArea.setComponentOrientation ( ComponentOrientation.RIGHT_TO_LEFT );
            scrollPane = new JScrollPane ( wordsNotStemmedTextArea );
            scrollPane.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setHorizontalScrollBarPolicy ( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setBorder ( BorderFactory.createLoweredBevelBorder ( ) );
            tabbedPane.addTab ( " Words Not Stemmed ", scrollPane );

            // the stopwords tab
            rows = 10;
            columns = 60;
            stopwordsTextArea = new JTextArea ( rows, columns );
            stopwordsTextArea.setLineWrap ( true );
            stopwordsTextArea.setWrapStyleWord ( true );
            stopwordsTextArea.setComponentOrientation ( ComponentOrientation.RIGHT_TO_LEFT );
            scrollPane = new JScrollPane ( stopwordsTextArea );
            scrollPane.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setHorizontalScrollBarPolicy ( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setBorder ( BorderFactory.createLoweredBevelBorder ( ) );
            tabbedPane.addTab ( " Stopwords ", scrollPane );

            // the buttons
            statisticsWindowButtons = new StatisticsWindowButtons ( );
            panel.add ( statisticsWindowButtons, BorderLayout.SOUTH );
        }
    }

    //--------------------------------------------------------------------------

    protected class RootsWindowButtons extends JPanel
    {
        // the buttons
        JButton saveAsButton;
        JButton closeButton;

        // constructor
        RootsWindowButtons ( )
        {
            // the layout manager
            GridLayout gridLayout = new GridLayout ( );
            setLayout ( gridLayout );

            // the buttons
            saveAsButton = new JButton ( "Save As..." );
            add ( saveAsButton );
            closeButton = new JButton ( "Close" );
            add ( closeButton );
        }
    }

    //--------------------------------------------------------------------------

    protected class RootsWindow extends JFrame
    {
        // the text area
        JTextArea textArea;

        // the buttons
        RootsWindowButtons rootsWindowButtons;

        // constructor
        RootsWindow ( )
        {
            // change the window icon
            String pathToImage = new StringBuffer ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "Images" + System.getProperty ( "file.separator" ) + "WindowIcon.gif" ).toString ( );
            Toolkit toolkit = Toolkit.getDefaultToolkit ( );
            Image image = toolkit.getImage ( pathToImage );
            setIconImage ( image );

            // set the window title
            setTitle ( " Roots " );

            // the panel
            JPanel panel = new JPanel ( );
            setContentPane( panel );

            // the layout manager
            final int horizontalGap = 0;
            final int verticalGap = 12;
            BorderLayout borderLayout = new BorderLayout ( horizontalGap, verticalGap );
            panel.setLayout ( borderLayout );

            // the border
            final int left = 11;
            final int right = left;
            final int top = left;
            final int bottom = left;
            panel.setBorder ( BorderFactory.createEmptyBorder ( top, left, bottom, right ) );

            // the text area
            int rows = 10;
            int columns = 60;
            textArea = new JTextArea ( rows, columns );
            textArea.setLineWrap ( true );
            textArea.setWrapStyleWord ( true );
            textArea.setComponentOrientation ( ComponentOrientation.RIGHT_TO_LEFT );
            JScrollPane scrollPane = new JScrollPane ( textArea );
            scrollPane.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setHorizontalScrollBarPolicy ( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            scrollPane.setBorder ( BorderFactory.createLoweredBevelBorder ( ) );
            panel.add ( scrollPane, BorderLayout.CENTER );

            // the buttons
            rootsWindowButtons = new RootsWindowButtons ( );
            panel.add ( rootsWindowButtons, BorderLayout.SOUTH );
        }
    }

    //--------------------------------------------------------------------------

    protected void resetStatusBarMessage ( )
    {
        // set the status bar message
        setStatusBarMessage ( "Ready" );
    }

    //--------------------------------------------------------------------------

    protected void setStatusBarMessage ( String message )
    {
        // set the status bar message
        components.statusBarPanel.label.setText ( " " + message + " " );
        components.statusBarPanel.label.paintImmediately ( 0, 0, components.statusBarPanel.label.getWidth ( ), components.statusBarPanel.label.getHeight ( ) );
    }

    //--------------------------------------------------------------------------

    protected void enableInputFilePanelButtons ( )
    {
        // enable the buttons
        components.inputFilePanel.inputFileButtons.closeButton.setEnabled ( true );
        components.inputFilePanel.inputFileButtons.saveButton.setEnabled ( true );
        components.inputFilePanel.inputFileButtons.saveAsButton.setEnabled ( true );
        components.outputFilePanel.outputFileButtons.stemButton.setEnabled ( true );
    }

    //--------------------------------------------------------------------------

    protected void disableInputFilePanelButtons ( )
    {
        // disable the buttons
        components.inputFilePanel.inputFileButtons.closeButton.setEnabled ( false );
        components.inputFilePanel.inputFileButtons.saveButton.setEnabled ( false );
        components.inputFilePanel.inputFileButtons.saveAsButton.setEnabled ( false );
        components.outputFilePanel.outputFileButtons.stemButton.setEnabled ( false );
    }

    //--------------------------------------------------------------------------

    protected void enableOutputFilePanelButtons ( )
    {
        // enable the buttons
        components.outputFilePanel.outputFileButtons.statisticsButton.setEnabled ( true );
        components.outputFilePanel.outputFileButtons.rootsButton.setEnabled ( true );
        components.outputFilePanel.outputFileButtons.saveAsButton.setEnabled ( true );
    }

    //--------------------------------------------------------------------------

    protected void disableOutputFilePanelButtons ( )
    {
        // disable the buttons
        components.outputFilePanel.outputFileButtons.statisticsButton.setEnabled ( false );
        components.outputFilePanel.outputFileButtons.rootsButton.setEnabled ( false );
        components.outputFilePanel.outputFileButtons.saveAsButton.setEnabled ( false );
        hideStatisticsWindow ( );
        hideRootsWindow ( );
    }

    //--------------------------------------------------------------------------

    protected void enableOutputFilePanelTextArea ( )
    {
        // enable the text area
        components.outputFilePanel.textArea.setVisible ( true );
        components.outputFilePanel.textArea.paintImmediately ( 0, 0, components.outputFilePanel.textArea.getWidth ( ), components.outputFilePanel.textArea.getHeight ( ) );
    }

    //--------------------------------------------------------------------------

    protected void disableOutputFilePanelTextArea ( )
    {
        // disable the text area
        components.outputFilePanel.textArea.setVisible ( false );
        components.outputFilePanel.textArea.paintImmediately ( 0, 0, components.outputFilePanel.textArea.getWidth ( ), components.outputFilePanel.textArea.getHeight ( ) );
    }

    //--------------------------------------------------------------------------

    protected File displayOpenFileDialog ( Component parent, File openInDirectory )
    {
        // display the open file dialog
        JFileChooser fileChooser = new JFileChooser ( );
        fileChooser.setDialogTitle ( " Open " );
        fileChooser.setPreferredSize ( new Dimension ( fileChooser.getPreferredSize( ).width + 15, fileChooser.getPreferredSize( ).height ) );
        if ( openInDirectory != null )
        {
            fileChooser.setCurrentDirectory ( openInDirectory );
        }
        else
        {
            fileChooser.setCurrentDirectory ( new File ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "ArabicTexts" ) );
        }
        int returnValue = fileChooser.showOpenDialog ( parent );
        if ( returnValue == JFileChooser.APPROVE_OPTION )
        {
            return fileChooser.getSelectedFile ( );
        }
        else
        {
            return null;
        }
    }

    //--------------------------------------------------------------------------

    protected File displaySaveAsFileDialog ( Component parent, File saveAsFile )
    {
        // display the save as file dialog
        JFileChooser fileChooser = new JFileChooser ( );
        fileChooser.setDialogTitle ( " Save As " );
        fileChooser.setPreferredSize ( new Dimension ( fileChooser.getPreferredSize( ).width + 15, fileChooser.getPreferredSize( ).height ) );
        if ( saveAsFile != null )
        {
            fileChooser.setSelectedFile ( saveAsFile );
        }
        else
        {
            fileChooser.setCurrentDirectory ( new File ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "ArabicTexts" ) );
        }
        int returnValue = fileChooser.showSaveDialog ( parent );
        if ( returnValue == JFileChooser.APPROVE_OPTION )
        {
            return fileChooser.getSelectedFile ( );
        }
        else
        {
            return null;
        }
    }

    //--------------------------------------------------------------------------

    protected void displayStatisticsWindow ( )
    {
        if ( statisticsWindow.isVisible ( ) != true )
        {
            // display the statistics window
            statisticsWindow.setVisible ( true );

            // centre the statistics window in the main window
            Dimension parentWindowSize = this.getSize ( );
            Point parentWindowLocation = this.getLocation ( );
            statisticsWindow.setLocation ( parentWindowLocation.x + ( parentWindowSize.width / 2 ) - ( statisticsWindow.getWidth ( ) / 2 ), parentWindowLocation.y + ( parentWindowSize.height / 2 ) - ( statisticsWindow.getHeight ( ) / 2 ) );

            // set the default button ( note : this MUST be done after the
            // window is visible on the screen )
            statisticsWindow.getRootPane( ).setDefaultButton ( statisticsWindow.statisticsWindowButtons.closeButton );
            statisticsWindow.statisticsWindowButtons.closeButton.requestFocus ( );
        }
        else
        {
            // bring the statistics window to the front
            statisticsWindow.toFront ( );
        }
    }

    //--------------------------------------------------------------------------

    protected void hideStatisticsWindow ( )
    {
        if ( statisticsWindow != null )
        {
            // hide the statistics window
            statisticsWindow.setVisible ( false );
        }
    }

    //--------------------------------------------------------------------------

    protected void displayRootsWindow ( )
    {
        if ( rootsWindow.isVisible ( ) != true )
        {
            // display the roots window
            rootsWindow.setVisible ( true );

            // centre the roots window in the main window
            Dimension parentWindowSize = this.getSize ( );
            Point parentWindowLocation = this.getLocation ( );
            rootsWindow.setLocation ( parentWindowLocation.x + ( parentWindowSize.width / 2 ) - ( rootsWindow.getWidth ( ) / 2 ), parentWindowLocation.y + ( parentWindowSize.height / 2 ) - ( rootsWindow.getHeight ( ) / 2 ) );

            // set the default button ( note : this MUST be done after the
            // window is visible on the screen )
            rootsWindow.getRootPane( ).setDefaultButton ( rootsWindow.rootsWindowButtons.closeButton );
            rootsWindow.rootsWindowButtons.closeButton.requestFocus ( );
        }
        else
        {
            // bring the roots window to the front
            rootsWindow.toFront ( );
        }
    }

    //--------------------------------------------------------------------------

    protected void hideRootsWindow ( )
    {
        if ( rootsWindow != null )
        {
            // hide the roots window
            rootsWindow.setVisible ( false );
        }
    }

    //--------------------------------------------------------------------------

    protected void displayAboutDialog ( )
    {
        // display the about dialog
        StringBuffer pathToImage = new StringBuffer ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "Images" + System.getProperty ( "file.separator" ) + "AboutDialogImage.gif" );
        Toolkit toolkit = Toolkit.getDefaultToolkit ( );
        ImageIcon imageIcon = new ImageIcon ( toolkit.getImage ( pathToImage.toString ( ) ) );
        JOptionPane.showMessageDialog ( this, "Arabic Stemmer.\nCopyright Shereen Khoja, 2002.\nEmail : shereen26@yahoo.co.uk.", " About Arabic Stemmer ", JOptionPane.INFORMATION_MESSAGE, imageIcon );
    }

    //--------------------------------------------------------------------------

    // make strings that are safe to add to combo boxes ( see the java api
    // description for the JComboBox class for more information - look in the
    // description for the addItem method )
    protected Object makeSafeComboBoxString ( final String text )
    {
        return new Object ( )
        {
            public String toString ( )
            {
                return text;
            }
        };
    }

    //--------------------------------------------------------------------------

    // set the font
    protected boolean alreadyInSetFont = false; // we could have used
                                                // synchronized instead of this,
                                                // but then we wouldn't have
                                                // been able to skip the whole
                                                // thing using the if statement
    protected void setFont ( )
    {
        if ( alreadyInSetFont == false )
        {
            alreadyInSetFont = true;
            setStatusBarMessage ( "Changing Font" );

            // create the font
            int previousFontSize = Integer.valueOf ( components.comboBoxesPanel.fontSizeComboBox.getSelectedItem( ).toString ( ) ).intValue ( );
            Font font = new Font ( components.comboBoxesPanel.fontComboBox.getSelectedItem( ).toString ( ), Font.PLAIN, previousFontSize );
            if ( font == null )
            {
                font = components.inputFilePanel.textArea.getFont ( );
            }

            // set the values in the font size combo box
            components.comboBoxesPanel.fontSizeComboBox.removeAllItems ( );
            for ( int size = 10; size < 32; size = size + 2 )
            {
                if ( font.deriveFont ( size ) != null )
                {
                    components.comboBoxesPanel.fontSizeComboBox.addItem ( makeSafeComboBoxString ( new StringBuffer( ).append( size ).toString ( ) ) );
                    if ( size == previousFontSize )
                    {
                        components.comboBoxesPanel.fontSizeComboBox.setSelectedIndex ( components.comboBoxesPanel.fontSizeComboBox.getItemCount ( ) - 1 );
                    }
                }
            }

            // set the font in the text areas
            components.inputFilePanel.textArea.setFont ( font );
            components.outputFilePanel.textArea.setFont ( font );
            statisticsWindow.stemmedWordsTextArea.setFont ( font );
            statisticsWindow.wordsNotStemmedTextArea.setFont ( font );
            statisticsWindow.stopwordsTextArea.setFont ( font );
            rootsWindow.textArea.setFont ( font );

            // repack the window components to reflect the new font size
            statisticsWindow.pack ( );
            rootsWindow.pack ( );
            this.pack ( );

            // centre the window in the screen
            Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize ( );
            setLocation ( ( screenSize.width / 2 ) - ( this.getWidth ( ) / 2 ), ( screenSize.height / 2 ) - ( this.getHeight ( ) / 2 ) );

            resetStatusBarMessage ( );
            alreadyInSetFont = false;
        }
    }

    //--------------------------------------------------------------------------

    // set the look and feel
    protected int changingFromIndex = 0, changingToIndex;
    protected void setLookAndFeel ( )
    {
        setStatusBarMessage ( "Changing Look and Feel" );
        if ( components.comboBoxesPanel.lookAndFeelComboBox.getSelectedItem ( ) != null )
        {
            try
            {
                if ( components.comboBoxesPanel.lookAndFeelComboBox.getSelectedItem( ).toString ( ) == "System" )
                {
                    changingToIndex = 0;
                    UIManager.setLookAndFeel ( UIManager.getSystemLookAndFeelClassName ( ) );
                }
                else if ( components.comboBoxesPanel.lookAndFeelComboBox.getSelectedItem( ).toString ( ) == "Motif" )
                {
                    changingToIndex = 1;
                    UIManager.setLookAndFeel ( "com.sun.java.swing.plaf.motif.MotifLookAndFeel" );
                }
                else
                {
                    changingToIndex = 2;
                    UIManager.setLookAndFeel ( UIManager.getCrossPlatformLookAndFeelClassName ( ) );
                }
                changingFromIndex = components.comboBoxesPanel.lookAndFeelComboBox.getSelectedIndex ( );

                // repack the window components to reflect the new look and feel
                // ( there is a problem with the motif look and feel - calling
                // updateComponentTreeUI for the look and feel combo box before
                // calling it for the frame avoids a null pointer exception
                // occurring during java's event dispatching ) ( the motif look
                // and feel problem is also responsible for all this messing
                // about with changingFromIndex and changingToIndex )
                SwingUtilities.updateComponentTreeUI ( components.comboBoxesPanel.lookAndFeelComboBox );
                SwingUtilities.updateComponentTreeUI ( this );
                SwingUtilities.updateComponentTreeUI ( statisticsWindow );
                SwingUtilities.updateComponentTreeUI ( rootsWindow );
                statisticsWindow.pack ( );
                rootsWindow.pack ( );
                this.pack ( );

                // centre the window in the screen
                Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize ( );
                setLocation ( ( screenSize.width / 2 ) - ( this.getWidth ( ) / 2 ), ( screenSize.height / 2 ) - ( this.getHeight ( ) / 2 ) );
            }
            catch ( Exception exception )
            {
                JOptionPane.showMessageDialog ( this, "Could not set the look and feel.", " Error ", JOptionPane.ERROR_MESSAGE );
                components.comboBoxesPanel.lookAndFeelComboBox.setSelectedIndex ( changingFromIndex );
            }
        }
        else
        {
            components.comboBoxesPanel.lookAndFeelComboBox.setSelectedIndex ( changingToIndex );
        }
        resetStatusBarMessage ( );
    }

    //--------------------------------------------------------------------------

    // constructor
    ArabicStemmerGUI ( ArabicStemmer arabicStemmerParameter )
    {
        // save a pointer to the arabic stemmer
        arabicStemmer = arabicStemmerParameter;

        // change the window icon
        String pathToImage = new StringBuffer ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "Images" + System.getProperty ( "file.separator" ) + "WindowIcon.gif" ).toString ( );
        Toolkit toolkit = Toolkit.getDefaultToolkit ( );
        Image image = toolkit.getImage ( pathToImage );
        setIconImage ( image );

        // set the window title
        setTitle ( defaultWindowTitle );

        // create the components and add them to the window
        components = new Components ( );
        setContentPane ( components );

        // set the status bar message
        resetStatusBarMessage ( );

        // disable the buttons
        disableInputFilePanelButtons ( );
        disableOutputFilePanelButtons ( );

        // disable the output file panel text area
        disableOutputFilePanelTextArea ( );

        // create the statistics window
        statisticsWindow = new StatisticsWindow ( );

        // create the roots window
        rootsWindow = new RootsWindow ( );

        // set the font
        setFont ( );

        // set the look and feel
        setLookAndFeel ( );

        // add some action listeners
        components.inputFilePanel.inputFileButtons.openButton.addActionListener ( arabicStemmer );
        components.inputFilePanel.inputFileButtons.closeButton.addActionListener ( arabicStemmer );
        components.inputFilePanel.inputFileButtons.saveButton.addActionListener ( arabicStemmer );
        components.inputFilePanel.inputFileButtons.saveAsButton.addActionListener ( arabicStemmer );
        components.outputFilePanel.outputFileButtons.stemButton.addActionListener ( arabicStemmer );
        components.outputFilePanel.outputFileButtons.statisticsButton.addActionListener ( arabicStemmer );
        components.outputFilePanel.outputFileButtons.rootsButton.addActionListener ( arabicStemmer );
        components.outputFilePanel.outputFileButtons.saveAsButton.addActionListener ( arabicStemmer );
        components.comboBoxesPanel.fontComboBox.addActionListener ( arabicStemmer );
        components.comboBoxesPanel.fontSizeComboBox.addActionListener ( arabicStemmer );
        components.comboBoxesPanel.lookAndFeelComboBox.addActionListener ( arabicStemmer );
        components.remainingButtonsPanel.remainingButtons.aboutButton.addActionListener ( arabicStemmer );
        components.remainingButtonsPanel.remainingButtons.exitButton.addActionListener ( arabicStemmer );
        statisticsWindow.statisticsWindowButtons.saveAsButton.addActionListener ( arabicStemmer );
        statisticsWindow.statisticsWindowButtons.closeButton.addActionListener ( arabicStemmer );
        rootsWindow.rootsWindowButtons.saveAsButton.addActionListener ( arabicStemmer );
        rootsWindow.rootsWindowButtons.closeButton.addActionListener ( arabicStemmer );
        components.inputFilePanel.textArea.getDocument( ).addDocumentListener ( arabicStemmer );

        // add a window listener that is called when the user attempts to close
        // the window
        setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
        addWindowListener ( new WindowAdapter ( )
        {
            public void windowClosing ( WindowEvent event )
            {
                arabicStemmer.remainingButtonsPanelExitButtonActionPerformed ( );
            }
        });

        // display the window
        setVisible ( true );

        // set the default button ( note : this MUST be done after the window is
        // visible on the screen )
        getRootPane( ).setDefaultButton ( components.inputFilePanel.inputFileButtons.openButton );
        components.inputFilePanel.inputFileButtons.openButton.requestFocus ( );
    }

    //--------------------------------------------------------------------------
}