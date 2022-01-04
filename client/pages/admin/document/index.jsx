import React, { useMemo, useEffect, useState } from "react";
import { useTable, usePagination } from "react-table";
import { COLUMN_DOCUMENT } from "../../../package/document/columns/ColumnDocument";

import { AdminLayout } from "../../../layouts/Admin";
import { documentService } from "../../../package/RestConnector";

const ManageDocument = () => {
  const [listDocument, setListDocument] = useState([]);
  const [isShowInfo, setIsShowInfo] = useState(false);
  const [dataEdit, setDataEdit] = useState([]);

  const columns = useMemo(() => COLUMN_DOCUMENT, []);
  const data = useMemo(() => listDocument, [listDocument]);
  const tableInstance = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0 },
    },
    usePagination
  );

  useEffect(() => {
    getData();
  }, []);

  const getData = async () => {
    try {
      const data = await documentService.getListDocumentByAdmin();
      console.log("data", data);
      setListDocument(data.data);
    } catch (err) {
      let msg;
      switch (err.code) {
        default: {
          msg = err.message;
        }
      }
      console.log("err", msg);
    }
  };

  const getProfileInfo = (data) => {
    console.log("data-cell", data.row.original);
    setDataEdit(data.row.original);
    setIsShowInfo(true);
  };

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
    page,
    canPreviousPage,
    canNextPage,
    pageOptions,
    pageCount,
    gotoPage,
    nextPage,
    previousPage,
    setPageSize,
    state: { pageIndex, pageSize },
  } = tableInstance;
  return (
    <>
      <div className="px-20 py-10">
        <div className="p-3 bg-white rounded-md shadow">
          <div className="mb-4 text-2xl font-semibold">List document</div>
          <table
            className=" w-full table text-gray-600 border-separate space-y-6 text-sm"
            {...getTableProps()}
          >
            <thead className="bg-gray-100 text-gray-500">
              {headerGroups.map((headerGroup, index) => (
                <tr key={index} {...headerGroup.getHeaderGroupProps()}>
                  {headerGroup.headers.map((column, index) => (
                    <th
                      className="p-3"
                      key={index}
                      {...column.getHeaderProps()}
                    >
                      {column.render("Header")}
                    </th>
                  ))}
                </tr>
              ))}
            </thead>
            <tbody {...getTableBodyProps()}>
              {page.map((row, i) => {
                prepareRow(row);
                return (
                  <tr className="bg-gray-100" key={i} {...row.getRowProps()}>
                    {row.cells.map((cell, index) => {
                      return (
                        <td
                          onClick={() => getProfileInfo(cell)}
                          className="p-3 text-center"
                          key={index}
                          {...cell.getCellProps()}
                          style={{ cursor: "pointer" }}
                        >
                          {cell.render("Cell")}
                        </td>
                      );
                    })}
                  </tr>
                );
              })}
            </tbody>
          </table>

          <div className="pagination flex items-center justify-center mt-3">
            <button
              className="py-3 px-5 bg-gradient-to-r from-pink-500 to-yellow-500 disabled:opacity-50 disabled:cursor-default border-2 rounded-l-lg ro border-yellow-500"
              onClick={() => gotoPage(0)}
              disabled={!canPreviousPage}
            >
              {"First"}
            </button>{" "}
            <button
              className="py-3 px-5 bg-gradient-to-r from-pink-500 to-yellow-500 disabled:opacity-50 disabled:cursor-default border-t-2 border-b-2 border-r-2 border-yellow-500"
              onClick={() => previousPage()}
              disabled={!canPreviousPage}
            >
              {"<"}
            </button>{" "}
            <div className="border-t-2 border-b-2 border-yellow-500">
              <span className="py-3 border-r-2 border-yellow-500 px-3">
                Page{" "}
                <strong>
                  {pageIndex + 1} of {pageOptions.length}
                </strong>{" "}
              </span>
              <span className="py-3 border-r-2 border-yellow-500 px-3">
                Go to page:{" "}
                <input
                  type="number"
                  className="border-2 border-yellow-500 pl-2"
                  defaultValue={pageIndex + 1}
                  onChange={(e) => {
                    const page = e.target.value
                      ? Number(e.target.value) - 1
                      : 0;
                    gotoPage(page);
                  }}
                  style={{ width: "100px" }}
                />
              </span>{" "}
              <select
                className="p-3 focus:outline-none"
                value={pageSize}
                onChange={(e) => {
                  setPageSize(Number(e.target.value));
                }}
              >
                {[10, 20, 30, 40, 50].map((pageSize) => (
                  <option key={pageSize} value={pageSize}>
                    Show {pageSize}
                  </option>
                ))}
              </select>
            </div>
            <button
              className="py-3 px-5 bg-gradient-to-r from-pink-500 to-yellow-500 disabled:opacity-50 disabled:cursor-default border-t-2 border-b-2 border-l-2 border-yellow-500"
              onClick={() => nextPage()}
              disabled={!canNextPage}
            >
              {">"}
            </button>{" "}
            <button
              className="py-3 px-5 bg-gradient-to-r from-pink-500 to-yellow-500 disabled:opacity-50 disabled:cursor-default border-2 rounded-r-lg ro border-yellow-500"
              onClick={() => gotoPage(pageCount - 1)}
              disabled={!canNextPage}
            >
              {"Last"}
            </button>{" "}
          </div>
        </div>
      </div>
    </>
  );
};

ManageDocument.getLayout = function getLayout(page) {
  return <AdminLayout>{page}</AdminLayout>;
};

export default ManageDocument;
