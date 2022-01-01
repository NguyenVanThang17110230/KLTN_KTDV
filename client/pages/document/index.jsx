import React, { useEffect, useState } from "react";
import Head from "next/head";
import { Formik, Field, Form } from "formik";

import { UserLayout } from "../../layouts/User";
import DocumentFile from "../../package/document/component/Document";
import AddNewDocumentModal from "../../package/document/component/AddNewDocumentModal";
import { documentService } from "../../package/RestConnector";

const Document = () => {
  const [isShowAddNewDocument, setIsShowAddNewDocument] = useState(false);
  const [document,setDocument] = useState([])
  useEffect(() => {
    getData()
  }, [])
  const getData = async() =>{
    try {
      const data = await documentService.getDocument();
      console.log('res',data);
      setDocument(data.data)

      // const list = await data.map(x => new Blob([new Uint8Array(x.content)], { type: 'application/pdf' }))
      // console.log('list',list);
      // const testUnit8 = new Uint8Array(data.data[0].contents)
      // let blob = new Blob([testUnit8], { type: 'application/pdf' });
      // window.open(URL.createObjectURL(blob))
      // setDocument(URL.createObjectURL(blob))
    } catch (err) {
      let msg;
      switch (err.code) {
        default: {
          msg = err.message;
        }
      }
      console.log("err", msg);
    }
  }
  return (
    <>
      <Head>
        <title>Document</title>
      </Head>
      <div className="px-20 py-10">
        <div className="shadow-md p-3 bg-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div className="font-medium text-2xl text-transparent bg-clip-text bg-gradient-to-r from-green-400 to-blue-500">
              List document
            </div>
            <a className="abc"></a>
            <div
              className="flex p-3 items-center bg-gradient-to-r from-green-400 to-blue-500 hover:from-pink-500 hover:to-yellow-500 cursor-pointer rounded-md relative"
              onClick={() => setIsShowAddNewDocument(true)}
              style={{ cursor: "pointer" }}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="icon icon-tabler icon-tabler-file-plus"
                width={26}
                height={26}
                viewBox="0 0 24 24"
                strokeWidth="1.5"
                stroke="#ffffff"
                fill="none"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                <path d="M14 3v4a1 1 0 0 0 1 1h4" />
                <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z" />
                <line x1={12} y1={11} x2={12} y2={17} />
                <line x1={9} y1={14} x2={15} y2={14} />
              </svg>
              <div className="ml-2 font-medium">Add new document</div>
            </div>
          </div>
          <DocumentFile file={document} />
        </div>
      </div>
      <AddNewDocumentModal
        style={isShowAddNewDocument}
        closeModal={() => setIsShowAddNewDocument(false)}
      />
    </>
  );
};

Document.getLayout = function getLayout(page) {
  return <UserLayout>{page}</UserLayout>;
};

export default Document;
