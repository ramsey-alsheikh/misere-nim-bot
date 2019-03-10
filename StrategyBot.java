import javax.lang.model.util.ElementScanner6;

public class StrategyBot
{
    private int[] line = new int[5];
    private Board field;

    public StrategyBot(Board f)
    {
        line[0] = 0;
        field = f;
        line[1] = field.getLineOne();
        line[2] = field.getLineTwo();
        line[3] = field.getLineThree();
        line[4] = field.getLineFour();
    }

    public void refreshLines()
    {
        line[1] = field.getLineOne();
        line[2] = field.getLineTwo();
        line[3] = field.getLineThree();
        line[4] = field.getLineFour();
    }

    public int getXORSum()
    {
        refreshLines();
        return line[1] ^ line[2] ^ line[3] ^ line[4];
    }

    public int[] getXORByte()
    {
        refreshLines();
        int[] groups = new int[3];

        if (getXORSum() == 1)
        {
            //001
            groups[2] = 1;
        }
        else if (getXORSum() == 2)
        {
            //010
            groups[1] = 1;
        }
        else if (getXORSum() == 3)
        {
            //011
            groups[2] = 1;
            groups[1] = 1;
        }
        else if (getXORSum() == 4)
        {
            //100
            groups[0] = 1;
        }
        else if (getXORSum() == 5)
        {
            //101
            groups[0] = 1;
            groups[2] = 1;
        }
        else if (getXORSum() == 6)
        {
            //110
            groups[0] = 1;
            groups[1] = 1;
        }
        else if (getXORSum() == 7)
        {
            //111
            groups[0] = 1;
            groups[1] = 1;
            groups[2] = 1;
        }

        return groups;
    }

    public int findFirstLineWithGroup(int x)
    {
        refreshLines();
        int cnt = 0, lineNum = 0;

        if (x == 1)
        {
            for (cnt = 1; cnt < line.length; cnt++)
            {
                if (line[cnt] % 2 == 1)
                    lineNum = cnt;
            }
        }

        else if (x == 2)
        {
            for (cnt = 1; cnt < line.length; cnt++)
            {
                if (line[cnt] != 0)
                {
                    if ((line[cnt] - 4) / 2.0 >= 1.0 && (line[cnt] != 4))
                    {
                        lineNum = cnt;
                    }
                    else if ((line[cnt] / 2.0) >= 1.0 && line[cnt] != 4)
                    {
                        lineNum = cnt;
                    }
                }
            }
        }

        else if (x == 4)
        {
            for (cnt = 1; cnt < line.length; cnt++)
            {
                if (line[cnt] - 4 >= 0)
                    lineNum = cnt;
            }
        }

        return lineNum;
    }

    public void makeMove(int ln, int nm)
    {
        refreshLines();
        field.take(ln, nm);
    }

    public void makeStrategicMove()
    {
        refreshLines();
        int[] groups = getXORByte();
        int cnt, firstGroup = 0, secondGroup = 0, lastGroup = 0;

        int nums = 0;

        for (cnt = 0; cnt < groups.length; cnt++)
        {
            if (groups[cnt] == 1)
            {
                firstGroup = getCntToGroup(cnt);
                break;
            }
        }

        for (cnt = 0; cnt < groups.length; cnt++)
        {
            if(groups[cnt] == 1 && nums < 2)
            {
                secondGroup = getCntToGroup(cnt);
                nums++;
            }
        }

        for (cnt = 0; cnt < groups.length; cnt++)
        {
            if (groups[cnt] == 1)
                lastGroup = getCntToGroup(cnt);
        }

       if (numberOfLines() == 2 && thereAre(1, 1))
       {
           makeMove(findFirstLineThatsNot(1), line[findFirstLineThatsNot(1)]);
       }
       else if (numberOfLines() == 2)
       {
            makeMove(findLargestLine(), line[findLargestLine()] - line[findSmallestLine()]);
       }
       else if (numberOfLines() == 3 && thereAre(2, 1))
       {
           makeMove(findFirstLineThatsNot(1), line[findFirstLineThatsNot(1)] - 1);
       }
       else if (numberOfLines() == 3 && howManyAreSame()[0] == 2)
       {
            makeMove(findFirstLineThatsNot(howManyAreSame()[1]), line[findFirstLineThatsNot(howManyAreSame()[1])]);
       }
       else if (getXORSum() == 7)
       {
            makeMove(findLargestLine(), 5);
       }
       else if (numberOfLines() == 3 && findFirstLineWith(7) != 0)
       {
           makeMove(findFirstLineWith(7), getXORSum());
       }
       else
       {
            makeMove(findFirstLineWithGroup(firstGroup), secondGroup + lastGroup);
       }
    }

    public int findFirstLineWith(int x)
    {
        refreshLines();
        int cnt;
        for (cnt = 1; cnt < line.length; cnt++)
        {
            if (line[cnt] - x >= 0)
                return cnt;
        }

        return 0;
    }

    public int getCntToGroup(int cnt)
    {
        refreshLines();
        int marker = cnt;

        switch (marker)
        {
            case 0:
                marker = 4;
                break;
            case 1:
                marker = 2;
                break;
            case 2:
                marker = 1;
                break;
            default:
                marker = 0;
        }

        return marker;
    }

    public boolean isGroup(int x)
    {
        refreshLines();
        boolean bool = false;

        switch (x)
        {
            case 1:
            case 2:
            case 4:
                bool = true;
                break;
        }

        return bool;
    }

    public int numberOfLines()
    {
        refreshLines();
        int sum = 0;
        for (int cnt = 1; cnt < line.length; cnt++)
        {
            if (line[cnt] > 0)
            {
                sum++;
            }
        }

        return sum;
    }

    public int firstLineOpen()
    {
        refreshLines();
        for (int cnt = 1; cnt <= 4; cnt++)
        {
            if (line[cnt] != 0)
                return cnt;
        }

        return 0;
    }

    public void state()
    {
        refreshLines();
        field.display();
        System.out.println(getXORSum());
    }

    public int findLargestLine()
    {
        refreshLines();
        int l = 0, c = 0;
        for (int cnt = 1; cnt < line.length; cnt++)
        {
            if (line[cnt] > l)
            {
                l = line[cnt];
                c = cnt;
            }
        }
        return c;
    }

    public int findSmallestLine()
    {
        refreshLines();
        int l = 7, c = 0;
        for (int cnt = 1; cnt < line.length; cnt++)
        {
            if (line[cnt] < l && line[cnt] > 0)
            {
                l = line[cnt];
                c = cnt;
            }
        }
        return c;
    }

    public void go()
    {
        refreshLines();
        makeStrategicMove();
        state();
    }

    public boolean thereAre(int howMany, int num)
    {
        refreshLines();
        int hhm = 0;
        for (int cnt = 1; cnt < line.length; cnt++)
        {
            if (line[cnt] == num)
            {
                hhm++;
            }
        }

        if (hhm == howMany)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public int findFirstLineThatsNot(int x)
    {
        for (int cnt = 1; cnt < line.length; cnt++)
        {
            if (line[cnt] != x)
            {
                return cnt;
            }
        }

        return 0;
    }

    public int[] howManyAreSame()
    {
        int same[] = {0,0};

        for (int cnt = 1; cnt < line.length; cnt++)
        {
            for (int cntTwo = (cnt + 1); cntTwo <= 4; cntTwo++)
            {
                if (line[cnt] == line[cntTwo] && line[cnt] != 0)
                {
                    same[0]++;
                    same[1] = line[cnt];
                }
            }
        }

        for (int cnt = 4; cnt > 0; cnt--)
        {
            for (int cntTwo = (cnt - 1); cntTwo > 0; cntTwo--)
            {
                if (line[cnt] == line[cntTwo] && line[cnt] != 0)
                {
                    same[0]++;
                    same[1] = line[cnt];
                }
            }
        }

        return same;
    }
}
