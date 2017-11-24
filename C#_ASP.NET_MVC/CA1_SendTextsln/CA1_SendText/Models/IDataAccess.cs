using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CA1_SendText.Models
{
    public interface IDataAccess<T>
    {
        IEnumerable<T> GetAllElements();
        bool FindElementById(Func<T, bool> findFunc);
        IEnumerable<T> GetAnyElements(Func<T, bool> func);
    }
}
