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
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class ArabicStemmer implements ActionListener, DocumentListener
{
    //--------------------------------------------------------------------------

    // the gui
    protected ArabicStemmerGUI arabicStemmerGUI;

    // a vector composed of vectors containing the static files
    protected Vector staticFiles;

    // the current input file
    protected boolean currentInputFilePanelFileNeedsSaving = false;
    protected File currentInputFilePanelFile;

    // the statistics for the stemmed text
    protected int [ ] stemmedTextStatistics;

    // the stemmed words, roots found, words not stemmed, and stopwords for the
    // stemmed text
    protected Vector stemmedTextLists = new Vector ( );

    // the possible roots for any unstemmed text
    protected int unstemmedTextNumberOfPossibleRoots;
    protected String [ ] [ ] unstemmedTextPossibleRoots;

    //--------------------------------------------------------------------------

    // execution starts here
    public static void main ( String [ ] args )
    {
        // create the stemmer
        ArabicStemmer arabicStemmer = new ArabicStemmer ( args[0] );
    }

    //--------------------------------------------------------------------------

    // constructor
    ArabicStemmer ( String s )
    {
        // create the gui
        //arabicStemmerGUI = new ArabicStemmerGUI ( this );

        // read in the static files
        readInStaticFiles ( );
        File currentInputFilePanelFile = new File(s);
        Stem stemmedText = new Stem ( currentInputFilePanelFile, staticFiles );
        System.out.print( stemmedText.displayText ( ) );
        
    }

    //--------------------------------------------------------------------------

    // handle input file panel open button actions
    protected void inputFilePanelOpenButtonActionPerformed ( )
    {
        // save the current input file
        boolean continueFlag = true;
        if ( currentInputFilePanelFileNeedsSaving == true )
        {
            int returnValue;
            if ( currentInputFilePanelFile == null )
            {
                returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Save the current input file before opening another?", " Save File ", JOptionPane.YES_NO_CANCEL_OPTION );
            }
            else
            {
                returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Save '" + currentInputFilePanelFile + "' before opening another input file?", " Save File ", JOptionPane.YES_NO_CANCEL_OPTION );
            }
            if ( returnValue == JOptionPane.YES_OPTION )
            {
                File tempFile = saveFile ( arabicStemmerGUI, currentInputFilePanelFile, arabicStemmerGUI.components.inputFilePanel.textArea, false );
                if ( tempFile == null )
                {
                    if ( currentInputFilePanelFile == null )
                    {
                        returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Current input file not saved. Open another anyway?", " Confirm ", JOptionPane.YES_NO_OPTION );
                    }
                    else
                    {
                        returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "'" + currentInputFilePanelFile + "' not saved. Open another input file anyway?", " Confirm ", JOptionPane.YES_NO_OPTION );
                    }
                    if ( returnValue == JOptionPane.NO_OPTION )
                    {
                        continueFlag = false;
                    }
                }
                else
                {
                    currentInputFilePanelFile = tempFile;
                    currentInputFilePanelFileNeedsSaving = false;
                    arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + currentInputFilePanelFile.getPath ( ) );
                }
            }
            else if ( returnValue == JOptionPane.CANCEL_OPTION )
            {
                continueFlag = false;
            }
        }

        // open a saved input file
        if ( continueFlag == true )
        {
            File tempFile = arabicStemmerGUI.displayOpenFileDialog ( arabicStemmerGUI, currentInputFilePanelFile );
            if ( tempFile != null )
            {
                try
                {
                    StringBuffer stringBuffer = new StringBuffer ( );
                    FileInputStream fileInputStream = new FileInputStream ( tempFile);
                    InputStreamReader inputStreamReader = new InputStreamReader ( fileInputStream, arabicStemmerGUI.components.comboBoxesPanel.encodingComboBox.getSelectedItem( ).toString ( ) );
                    Reader reader = new BufferedReader ( inputStreamReader );
                    int character;
                    while ( ( character = reader.read ( ) ) > -1 )
                    {
                        stringBuffer.append ( ( char ) character );
                    }
                    reader.close ( );

                    // put the file contents into the input file text area
                    arabicStemmerGUI.components.inputFilePanel.textArea.setText ( stringBuffer.toString ( ) );
                    arabicStemmerGUI.components.inputFilePanel.textArea.setCaretPosition ( 0 );

                    // enable the buttons
                    arabicStemmerGUI.enableInputFilePanelButtons ( );

                    // update the current input file variables
                    currentInputFilePanelFile = tempFile;
                    currentInputFilePanelFileNeedsSaving = false;

                    // set the window title
                    arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + currentInputFilePanelFile.getPath ( ) );
                }
                catch ( Exception exception )
                {
                    JOptionPane.showMessageDialog ( arabicStemmerGUI, "Could not open '" + tempFile + "'.", " Error ", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    //--------------------------------------------------------------------------

    // handle input file panel close button actions
    protected void inputFilePanelCloseButtonActionPerformed ( )
    {
        // save the current input file
        boolean continueFlag = true;
        if ( currentInputFilePanelFileNeedsSaving == true )
        {
            int returnValue;
            if ( currentInputFilePanelFile == null )
            {
                returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Save the current input file before closing it?", " Save File ", JOptionPane.YES_NO_CANCEL_OPTION );
            }
            else
            {
                returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Save '" + currentInputFilePanelFile + "' before closing it?", " Save File ", JOptionPane.YES_NO_CANCEL_OPTION );
            }
            if ( returnValue == JOptionPane.YES_OPTION )
            {
                File tempFile = saveFile ( arabicStemmerGUI, currentInputFilePanelFile, arabicStemmerGUI.components.inputFilePanel.textArea, false );
                if ( tempFile == null )
                {
                    if ( currentInputFilePanelFile == null )
                    {
                        returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Current input file not saved. Close it anyway?", " Confirm ", JOptionPane.YES_NO_OPTION );
                    }
                    else
                    {
                        returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "'" + currentInputFilePanelFile + "' not saved. Close it anyway?", " Confirm ", JOptionPane.YES_NO_OPTION );
                    }
                    if ( returnValue == JOptionPane.NO_OPTION )
                    {
                        continueFlag = false;
                    }
                }
                else
                {
                    currentInputFilePanelFile = tempFile;
                    currentInputFilePanelFileNeedsSaving = false;
                    arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + currentInputFilePanelFile.getPath ( ) );
                }
            }
            else if ( returnValue == JOptionPane.CANCEL_OPTION )
            {
                continueFlag = false;
            }
        }

        // close the current input file
        if ( continueFlag == true )
        {
            // clear the input file text area
            arabicStemmerGUI.components.inputFilePanel.textArea.setText ( "" );

            // clear the output file text area
            arabicStemmerGUI.components.outputFilePanel.textArea.setText ( "" );
            arabicStemmerGUI.disableOutputFilePanelTextArea ( );

            // disable the buttons
            arabicStemmerGUI.disableInputFilePanelButtons ( );
            arabicStemmerGUI.disableOutputFilePanelButtons ( );

            // update the current input file variables
            currentInputFilePanelFile = null;
            currentInputFilePanelFileNeedsSaving = false;

            // set the window title
            arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle );
        }
    }

    //--------------------------------------------------------------------------

    // handle input file panel save button actions
    protected void inputFilePanelSaveButtonActionPerformed ( )
    {
        File tempFile = saveFile ( arabicStemmerGUI, currentInputFilePanelFile, arabicStemmerGUI.components.inputFilePanel.textArea, false );
        if ( tempFile != null )
        {
            currentInputFilePanelFile = tempFile;
            currentInputFilePanelFileNeedsSaving = false;
            arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + currentInputFilePanelFile.getPath ( ) );
        }
    }

    //--------------------------------------------------------------------------

    // handle input file panel save as button actions
    protected void inputFilePanelSaveAsButtonActionPerformed ( )
    {
        File tempFile = saveFile ( arabicStemmerGUI, currentInputFilePanelFile, arabicStemmerGUI.components.inputFilePanel.textArea, true );
        if ( tempFile != null )
        {
            currentInputFilePanelFile = tempFile;
            currentInputFilePanelFileNeedsSaving = false;
            arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + currentInputFilePanelFile.getPath ( ) );
        }
    }

    //--------------------------------------------------------------------------

    // handle output file panel stem button actions
    protected void outputFilePanelStemButtonActionPerformed ( )
    {
        // save the current input file
        if ( currentInputFilePanelFile == null )
        {
            JOptionPane.showMessageDialog ( arabicStemmerGUI, "The input file must be saved before stemming.", " Information ", JOptionPane.INFORMATION_MESSAGE );
            inputFilePanelSaveAsButtonActionPerformed ( );
        }
        else if ( currentInputFilePanelFileNeedsSaving == true )
        {
            int returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Save '" + currentInputFilePanelFile + "'?", " Save File ", JOptionPane.YES_NO_OPTION );
            if ( returnValue == JOptionPane.YES_OPTION )
            {
                inputFilePanelSaveButtonActionPerformed ( );
            }
        }

        // stem the current input file
        if ( ( currentInputFilePanelFile == null ) || ( currentInputFilePanelFileNeedsSaving == true ) )
        {
            JOptionPane.showMessageDialog ( arabicStemmerGUI, "Unable to stem as the input file was not saved.", " Information ", JOptionPane.INFORMATION_MESSAGE );
        }
        else
        {
            // set the status bar message
            arabicStemmerGUI.setStatusBarMessage ( "Stemming" );

            try
            {
                // stem the current input file
                arabicStemmerGUI.components.outputFilePanel.outputFileButtons.stemButton.paintImmediately ( 0, 0, arabicStemmerGUI.components.outputFilePanel.outputFileButtons.stemButton.getWidth ( ), arabicStemmerGUI.components.outputFilePanel.outputFileButtons.stemButton.getHeight ( ) );
                long startTime = System.currentTimeMillis ( );
                Stem stemmedText = new Stem ( currentInputFilePanelFile, staticFiles );
                long finishTime = System.currentTimeMillis ( );

                // check if the stemmer could not open the input file
                if ( stemmedText.couldNotOpenFile == true )
                {
                    JOptionPane.showMessageDialog ( arabicStemmerGUI, "The input file could not be stemmed as the\nstemmer could not open the file. This is\nprobably a file encoding problem. Save the\ninput file under a different encoding and\ntry again.", " Information ", JOptionPane.INFORMATION_MESSAGE );
                }
                else
                {
                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Retrieving Stemmed Text" );

                    // display the stemmed text in the output file text area
                    arabicStemmerGUI.components.outputFilePanel.textArea.setText ( stemmedText.displayText ( ) );
                    arabicStemmerGUI.components.outputFilePanel.textArea.setCaretPosition ( 0 );
                    arabicStemmerGUI.enableOutputFilePanelTextArea ( );

                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Retrieving Statistics Window Data" );

                    // get the statistics for the stemmed text
                    stemmedTextStatistics = stemmedText.returnNumberStatistics ( );
                    stemmedTextLists = stemmedText.returnLists ( );
                    unstemmedTextNumberOfPossibleRoots = stemmedText.returnNoPossibleRoots ( );
                    unstemmedTextPossibleRoots = stemmedText.returnPossibleRoots ( );

                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Updating Statistics Window ( Statistics )" );

                    // put the stemming statistics into the panel in the
                    // statistics window
                    float totalWords = new Float ( String.valueOf ( stemmedTextStatistics [ 0 ] ) ).floatValue ( );
                    float wordsNotStemmed = new Float ( String.valueOf ( stemmedTextStatistics [ 2 ] ) ).floatValue ( );
                    String percentStemmed = new Float ( ( ( totalWords - wordsNotStemmed ) / totalWords ) * 100 ).toString ( );
                    if ( percentStemmed.lastIndexOf ( "." ) != -1 )
                    {
                        if ( ( percentStemmed.length ( ) - percentStemmed.lastIndexOf ( "." ) ) > 2 )
                        {
                            percentStemmed = percentStemmed.substring ( 0, percentStemmed.lastIndexOf ( "." ) + 3 );
                        }
                    }
                    if ( percentStemmed.compareTo ( "NaN" ) == 0 )
                    {
                        percentStemmed = " Error ";
                    }
                    else
                    {
                        percentStemmed = " " + percentStemmed + " % ";
                    }
                    String stemmingTime = " " + new Long ( ( finishTime - startTime ) / 1000 ).toString ( );
                    stemmingTime = stemmingTime + ".";
                    stemmingTime = stemmingTime + new Long ( ( finishTime - startTime ) % 1000 ).toString ( );
                    stemmingTime = stemmingTime + " Seconds ";
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.percentStemmedLabel.setText ( percentStemmed );
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.stemmingTimeLabel.setText ( stemmingTime );
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.stemmedWordsLabel.setText ( " " + String.valueOf ( stemmedTextStatistics [ 1 ] ) + " " );
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.wordsNotStemmedLabel.setText ( " " + String.valueOf ( stemmedTextStatistics [ 2 ] ) + " " );
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.stopwordsLabel.setText ( " " + String.valueOf ( stemmedTextStatistics [ 3 ] ) + " " );
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.punctuationWordsLabel.setText ( " " + String.valueOf ( stemmedTextStatistics [ 4 ] ) + " " );
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.nonLetterWordsLabel.setText ( " " + String.valueOf ( stemmedTextStatistics [ 5 ] ) + " " );
                    arabicStemmerGUI.statisticsWindow.statisticsWindowLabels.totalWordsLabel.setText ( " " + String.valueOf ( stemmedTextStatistics [ 0 ] ) + " " );

                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Updating Statistics Window ( Stemmed Words )" );

                    // put the stemmed words and their roots into the stemmed
                    // words tab in the statistics window
                    Vector stemmedWords = ( Vector ) stemmedTextLists.elementAt ( 0 );
                    Vector rootsFound = ( Vector ) stemmedTextLists.elementAt ( 1 );
                    StringBuffer tempStringBuffer = new StringBuffer ( );
                    for ( int index = 0; index < stemmedWords.size ( ); index ++ )
                    {
                        tempStringBuffer.append ( "[ " + ( String ) stemmedWords.elementAt ( index ) + " : " + ( String ) rootsFound.elementAt ( index ) + " ]    " );
                    }
                    arabicStemmerGUI.statisticsWindow.stemmedWordsTextArea.setText ( tempStringBuffer.toString ( ) );
                    arabicStemmerGUI.statisticsWindow.stemmedWordsTextArea.setCaretPosition ( 0 );

                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Updating Statistics Window ( Words Not Stemmed )" );

                    // put the words not stemmed into the words not stemmed tab
                    // in the statistics window
                    Vector notStemmedWords = ( Vector ) stemmedTextLists.elementAt ( 2 );
                    tempStringBuffer = new StringBuffer ( );
                    for ( int index = 0; index < notStemmedWords.size ( ); index ++ )
                    {
                        tempStringBuffer.append ( ( String ) notStemmedWords.elementAt ( index ) + "    " );
                    }
                    arabicStemmerGUI.statisticsWindow.wordsNotStemmedTextArea.setText ( tempStringBuffer.toString ( ) );
                    arabicStemmerGUI.statisticsWindow.wordsNotStemmedTextArea.setCaretPosition ( 0 );

                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Updating Statistics Window ( Stopwords )" );

                    // put the stopwords into the stopwords tab in the
                    // statistics window
                    Vector originalStopword = ( Vector ) stemmedTextLists.elementAt ( 3 );
                    Vector stopwordsFound = ( Vector ) stemmedTextLists.elementAt ( 4 );
                    tempStringBuffer = new StringBuffer ( );
                    for ( int index = 0; index < originalStopword.size ( ); index ++ )
                    {
                        tempStringBuffer.append ( "[ " + ( String ) originalStopword.elementAt ( index ) + " : " + ( String ) stopwordsFound.elementAt ( index ) + " ]    " );
                    }
                    arabicStemmerGUI.statisticsWindow.stopwordsTextArea.setText ( tempStringBuffer.toString ( ) );
                    arabicStemmerGUI.statisticsWindow.stopwordsTextArea.setCaretPosition ( 0 );

                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Updating Roots Window" );

                    // put the possible roots for any unstemmed text into the
                    // text area in the roots window
                    tempStringBuffer = new StringBuffer ( );
                    for ( int index = 0; index < unstemmedTextNumberOfPossibleRoots; index ++ )
                    {
                        if ( unstemmedTextPossibleRoots [ index ] [ 1 ].compareTo ( "" ) != 0 )
                        {
                            tempStringBuffer.append ( "[ " + unstemmedTextPossibleRoots [ index ] [ 0 ] + " : " + unstemmedTextPossibleRoots [ index ] [ 1 ] + " ]    " );
                        }
                    }
                    arabicStemmerGUI.rootsWindow.textArea.setText ( tempStringBuffer.toString ( ) );
                    arabicStemmerGUI.rootsWindow.textArea.setCaretPosition ( 0 );

                    // set the status bar message
                    arabicStemmerGUI.setStatusBarMessage ( "Checking Results" );

                    // enable or disable the buttons depending on the success of
                    // the stemming
                    if ( stemmedTextStatistics [ 0 ] == 0 )
                    {
                        JOptionPane.showMessageDialog ( arabicStemmerGUI, "The input file could not be stemmed as it contains no words.", " Information ", JOptionPane.INFORMATION_MESSAGE );

                        // disable the buttons
                        arabicStemmerGUI.disableOutputFilePanelButtons ( );
                    }
                    else
                    {
                        // enable the buttons
                        arabicStemmerGUI.enableOutputFilePanelButtons ( );
                    }
                }
            }
            catch ( Exception exception )
            {
                JOptionPane.showMessageDialog ( arabicStemmerGUI, "The input file could not be stemmed as it is\ntoo long for Java to process. You should split\nit into smaller pieces and stem each piece\nindividually.", " Error ", JOptionPane.ERROR_MESSAGE );
            }

            // set the status bar message
            arabicStemmerGUI.resetStatusBarMessage ( );
        }
    }

    //--------------------------------------------------------------------------

    // handle output file panel statistics button actions
    protected void outputFilePanelStatisticsButtonActionPerformed ( )
    {
        arabicStemmerGUI.displayStatisticsWindow ( );
    }

    //--------------------------------------------------------------------------

    // handle output file panel roots button actions
    protected void outputFilePanelRootsButtonActionPerformed ( )
    {
        arabicStemmerGUI.displayRootsWindow ( );
    }

    //--------------------------------------------------------------------------

    // handle output file panel save as button actions
    protected void outputFilePanelSaveAsButtonActionPerformed ( )
    {
        saveFile ( arabicStemmerGUI, new File ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "ArabicTexts" + System.getProperty ( "file.separator" ) + "StemmedText.txt" ), arabicStemmerGUI.components.outputFilePanel.textArea, true );
    }

    //--------------------------------------------------------------------------

    // handle combo boxes panel font combo box actions
    protected void comboBoxesPanelFontComboBoxActionPerformed ( )
    {
        arabicStemmerGUI.setFont ( );
    }

    //--------------------------------------------------------------------------

    // handle combo boxes panel look and feel combo box actions
    protected void comboBoxesPanelLookAndFeelComboBoxActionPerformed ( )
    {
        arabicStemmerGUI.setLookAndFeel ( );
    }

    //--------------------------------------------------------------------------

    // handle remaining buttons panel about button actions
    protected void remainingButtonsPanelAboutButtonActionPerformed ( )
    {
        arabicStemmerGUI.displayAboutDialog ( );
    }

    //--------------------------------------------------------------------------

    // handle remaining buttons panel exit button actions
    protected void remainingButtonsPanelExitButtonActionPerformed ( )
    {
        // save the current input file
        boolean continueFlag = true;
        if ( currentInputFilePanelFileNeedsSaving == true )
        {
            int returnValue;
            if ( currentInputFilePanelFile == null )
            {
                returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Save the current input file before exiting?", " Save File ", JOptionPane.YES_NO_CANCEL_OPTION );
            }
            else
            {
                returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Save '" + currentInputFilePanelFile + "' before exiting?", " Save File ", JOptionPane.YES_NO_CANCEL_OPTION );
            }
            if ( returnValue == JOptionPane.YES_OPTION )
            {
                File tempFile = saveFile ( arabicStemmerGUI, currentInputFilePanelFile, arabicStemmerGUI.components.inputFilePanel.textArea, false );
                if ( tempFile == null )
                {
                    if ( currentInputFilePanelFile == null )
                    {
                        returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "Current input file not saved. Exit anyway?", " Confirm ", JOptionPane.YES_NO_OPTION );
                    }
                    else
                    {
                        returnValue = JOptionPane.showConfirmDialog ( arabicStemmerGUI, "'" + currentInputFilePanelFile + "' not saved. Exit anyway?", " Confirm ", JOptionPane.YES_NO_OPTION );
                    }
                    if ( returnValue == JOptionPane.NO_OPTION )
                    {
                        continueFlag = false;
                    }
                }
                else
                {
                    currentInputFilePanelFile = tempFile;
                    currentInputFilePanelFileNeedsSaving = false;
                    arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + currentInputFilePanelFile.getPath ( ) );
                }
            }
            else if ( returnValue == JOptionPane.CANCEL_OPTION )
            {
                continueFlag = false;
            }
        }

        // exit the program
        if ( continueFlag == true )
        {
            System.exit ( 0 );
        }
    }

    //--------------------------------------------------------------------------

    // handle statistics window save as button actions
    protected void statisticsWindowSaveAsButtonActionPerformed ( )
    {
        if ( arabicStemmerGUI.statisticsWindow.tabbedPane.getSelectedIndex ( ) == 0 )
        {
            saveFile ( arabicStemmerGUI.statisticsWindow, new File ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "ArabicTexts" + System.getProperty ( "file.separator" ) + "StemmedWords.txt" ), arabicStemmerGUI.statisticsWindow.stemmedWordsTextArea, true );
        }
        else if ( arabicStemmerGUI.statisticsWindow.tabbedPane.getSelectedIndex ( ) == 1 )
        {
            saveFile ( arabicStemmerGUI.statisticsWindow, new File ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "ArabicTexts" + System.getProperty ( "file.separator" ) + "WordsNotStemmed.txt" ), arabicStemmerGUI.statisticsWindow.wordsNotStemmedTextArea, true );
        }
        else if ( arabicStemmerGUI.statisticsWindow.tabbedPane.getSelectedIndex ( ) == 2 )
        {
            saveFile ( arabicStemmerGUI.statisticsWindow, new File ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "ArabicTexts" + System.getProperty ( "file.separator" ) + "Stopwords.txt" ), arabicStemmerGUI.statisticsWindow.stopwordsTextArea, true );
        }
        arabicStemmerGUI.statisticsWindow.toFront ( );
    }

    //--------------------------------------------------------------------------

    // handle statistics window close button actions
    protected void statisticsWindowCloseButtonActionPerformed ( )
    {
        arabicStemmerGUI.hideStatisticsWindow ( );
    }

    //--------------------------------------------------------------------------

    // handle roots window save as button actions
    protected void rootsWindowSaveAsButtonActionPerformed ( )
    {
        saveFile ( arabicStemmerGUI.rootsWindow, new File ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "ArabicTexts" + System.getProperty ( "file.separator" ) + "Roots.txt" ), arabicStemmerGUI.rootsWindow.textArea, true );
        arabicStemmerGUI.rootsWindow.toFront ( );
    }

    //--------------------------------------------------------------------------

    // handle roots window close button actions
    protected void rootsWindowCloseButtonActionPerformed ( )
    {
        arabicStemmerGUI.hideRootsWindow ( );
    }

    //--------------------------------------------------------------------------

    // handle actions
    public void actionPerformed ( ActionEvent event )
    {
        if ( event.getSource ( ) == arabicStemmerGUI.components.inputFilePanel.inputFileButtons.openButton )
        {
            inputFilePanelOpenButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.inputFilePanel.inputFileButtons.closeButton )
        {
            inputFilePanelCloseButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.inputFilePanel.inputFileButtons.saveButton )
        {
            inputFilePanelSaveButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.inputFilePanel.inputFileButtons.saveAsButton )
        {
            inputFilePanelSaveAsButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.outputFilePanel.outputFileButtons.stemButton )
        {
            outputFilePanelStemButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.outputFilePanel.outputFileButtons.statisticsButton )
        {
            outputFilePanelStatisticsButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.outputFilePanel.outputFileButtons.rootsButton )
        {
            outputFilePanelRootsButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.outputFilePanel.outputFileButtons.saveAsButton )
        {
            outputFilePanelSaveAsButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.comboBoxesPanel.fontComboBox )
        {
            comboBoxesPanelFontComboBoxActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.comboBoxesPanel.fontSizeComboBox )
        {
            comboBoxesPanelFontComboBoxActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.comboBoxesPanel.lookAndFeelComboBox )
        {
            comboBoxesPanelLookAndFeelComboBoxActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.remainingButtonsPanel.remainingButtons.aboutButton )
        {
            remainingButtonsPanelAboutButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.components.remainingButtonsPanel.remainingButtons.exitButton )
        {
            remainingButtonsPanelExitButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.statisticsWindow.statisticsWindowButtons.saveAsButton )
        {
            statisticsWindowSaveAsButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.statisticsWindow.statisticsWindowButtons.closeButton )
        {
            statisticsWindowCloseButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.rootsWindow.rootsWindowButtons.saveAsButton )
        {
            rootsWindowSaveAsButtonActionPerformed ( );
        }
        else if ( event.getSource ( ) == arabicStemmerGUI.rootsWindow.rootsWindowButtons.closeButton )
        {
            rootsWindowCloseButtonActionPerformed ( );
        }
    }
    public void insertUpdate ( DocumentEvent event )
    {
        inputFilePanelTextAreaChanged ( );
    }
    public void removeUpdate ( DocumentEvent event )
    {
        inputFilePanelTextAreaChanged ( );
    }
    public void changedUpdate ( DocumentEvent event )
    {
        inputFilePanelTextAreaChanged ( );
    }
    protected void inputFilePanelTextAreaChanged ( )
    {
        if ( arabicStemmerGUI.components.inputFilePanel.textArea.getText( ).compareTo ( "" ) == 0 )
        {
            currentInputFilePanelFileNeedsSaving = false;
            inputFilePanelCloseButtonActionPerformed ( );
        }
        else
        {
            currentInputFilePanelFileNeedsSaving = true;
            if ( currentInputFilePanelFile == null )
            {
                arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + "*" );
            }
            else
            {
                arabicStemmerGUI.setTitle ( arabicStemmerGUI.defaultWindowTitle + ": " + currentInputFilePanelFile.getPath ( ) + " *" );
            }
            arabicStemmerGUI.enableInputFilePanelButtons ( );
        }
    }

    //--------------------------------------------------------------------------

    // save the contents of a text area to a file
    protected File saveFile ( Component parent, File file, JTextArea textArea, boolean doSaveAs )
    {
        if ( ( file == null ) || ( doSaveAs == true ) )
        {
            file = arabicStemmerGUI.displaySaveAsFileDialog ( parent, file );
            if ( file != null )
            {
                if ( file.exists ( ) == true )
                {
                    int returnValue = JOptionPane.showConfirmDialog ( parent, "Overwrite '" + file + "'?", " Confirm ", JOptionPane.YES_NO_OPTION );
                    if ( returnValue == JOptionPane.NO_OPTION )
                    {
                        file = null;
                    }
                }
            }
        }
        if ( file != null )
        {
            try
            {
                FileOutputStream fileOutputStream = new FileOutputStream ( file );
                Writer writer = new OutputStreamWriter ( fileOutputStream, arabicStemmerGUI.components.comboBoxesPanel.encodingComboBox.getSelectedItem( ).toString ( ) );
                writer.write ( textArea.getText ( ) );
                writer.close ( );
                JOptionPane.showMessageDialog ( parent, "Successfully saved '" + file + "'.", " Information ", JOptionPane.INFORMATION_MESSAGE );
            }
            catch ( Exception exception )
            {
                JOptionPane.showMessageDialog ( parent, "Could not save '" + file + "'.", " Error ", JOptionPane.ERROR_MESSAGE );
                file = null;
            }
        }
        return file;
    }

    //--------------------------------------------------------------------------

    // read in the static files
    protected void readInStaticFiles ( )
    {
        // create a string buffer containing the path to the static files
        String pathToStemmerFiles = new StringBuffer ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "StemmerFiles" + System.getProperty ( "file.separator" ) ).toString ( );

        // create the vector composed of vectors containing the static files
        staticFiles = new Vector ( );
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "definite_article.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "duplicate.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "first_waw.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "first_yah.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "last_alif.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "last_hamza.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "last_maksoura.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "last_yah.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "mid_waw.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "mid_yah.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "prefixes.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "punctuation.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "quad_roots.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "stopwords.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "suffixes.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "tri_patt.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "tri_roots.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "diacritics.txt" ).toString ( ) ) )
        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "strange.txt" ).toString ( ) ) )
        {
            // the vector was successfully created
            //System.out.println( "read in files successfully" );
        }
    }

    //--------------------------------------------------------------------------

    // read in the contents of a file, put it into a vector, and add that vector
    // to the vector composed of vectors containing the static files
    protected boolean addVectorFromFile ( String fileName )
    {
        boolean returnValue;
        try
        {
            // the vector we are going to fill
            Vector vectorFromFile = new Vector ( );

            // create a buffered reader
            File file = new File ( fileName );
            FileInputStream fileInputStream = new FileInputStream ( file );
            InputStreamReader inputStreamReader = new InputStreamReader ( fileInputStream, "UTF-16" );

            //If the bufferedReader is not big enough for a file, I should change the size of it here
            BufferedReader bufferedReader = new BufferedReader ( inputStreamReader, 20000 );

            // read in the text a line at a time
            String part;
            StringBuffer word = new StringBuffer ( );
            while ( ( part = bufferedReader.readLine ( ) ) != null )
            {
                // add spaces at the end of the line
                part = part + "  ";

                // for each line
                for ( int index = 0; index < part.length ( ) - 1; index ++ )
                {
                    // if the character is not a space, append it to a word
                    if ( ! ( Character.isWhitespace ( part.charAt ( index ) ) ) )
                    {
                        word.append ( part.charAt ( index ) );
                    }
                    // otherwise, if the word contains some characters, add it
                    // to the vector
                    else
                    {
                        if ( word.length ( ) != 0 )
                        {
                            vectorFromFile.addElement ( word.toString ( ) );
                            word.setLength ( 0 );
                        }
                    }
                }
            }

            // trim the vector
            vectorFromFile.trimToSize ( );

            // destroy the buffered reader
            bufferedReader.close ( );
   	        fileInputStream.close ( );

            // add the vector to the vector composed of vectors containing the
            // static files
            staticFiles.addElement ( vectorFromFile );
            returnValue = true;
        }
        catch ( Exception exception )
        {
            JOptionPane.showMessageDialog ( arabicStemmerGUI, "Could not open '" + fileName + "'.", " Error ", JOptionPane.ERROR_MESSAGE );
            returnValue = false;
        }
        return returnValue;
    }

    //--------------------------------------------------------------------------
}
