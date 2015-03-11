package ch.heigvd.res.lab01.impl.explorers;

import ch.heigvd.res.lab01.interfaces.IFileExplorer;
import ch.heigvd.res.lab01.interfaces.IFileVisitor;
import java.io.File;
import javassist.compiler.ast.Visitor;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 * 
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor vistor) {
     vistor.visit(rootDirectory);
     
     // DFS of the directory's elements.
     if (rootDirectory.isDirectory())
     {
         for (File child : rootDirectory.listFiles())
            if (child.isFile())
               vistor.visit(child);
         
         for (File child : rootDirectory.listFiles())
            if (child.isDirectory())
               explore(child, vistor);
     }
  }

}
